package pl.greywarden.openr.gui.find;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.commons.PathComboBox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class FindWindow extends Stage {

    private final VBox layout = new VBox(5);
    private ListView<AbstractEntry> result;
    private final TextField input = new TextField();
    private PathComboBox pathComboBox;
    private CheckBox recursive;
    private Label pathLabel;
    private Label recursiveLabel;
    private HBox pathSelectionWrapper;
    private Button doSearch;

    public FindWindow() {
        super();
        layout.setPadding(new Insets(5));

        createSearchBar();
        createPathSelection();
        createResultList();

        super.setTitle(getString("find-file"));
        super.setScene(new Scene(layout));
        disableSearchButtonOnInvalidInput();
        show();
    }

    private void disableSearchButtonOnInvalidInput() {
        BooleanBinding binding = input.textProperty().isNotEmpty();
        doSearch.disableProperty().bind(binding.and(pathComboBox.pathValidBinding.not()).not());
    }

    private void createSearchBar() {
        HBox wrapper = new HBox(5);

        doSearch = createSearchButton();
        createInputTextField();

        HBox.setHgrow(input, Priority.ALWAYS);

        wrapper.getChildren().addAll(input, doSearch);
        layout.getChildren().add(wrapper);
    }

    private void createInputTextField() {
        input.setOnKeyPressed(this::handleInputKeyEvent);
    }

    private void handleInputKeyEvent(KeyEvent event) {
        if (!doSearch.isDisabled() && event.getCode().equals(KeyCode.ENTER)) {
            Platform.runLater(this::search);
        }
    }

    private Button createSearchButton() {
        Button doSearch = new Button();
        doSearch.setGraphic(IconManager.getProgramIcon("go"));
        doSearch.setOnAction(event -> Platform.runLater(this::search));
        return doSearch;
    }

    private void createResultList() {
        result = new ListView<>();
        result.setCellFactory(param -> new ListCell<AbstractEntry>() {
            @Override
            protected void updateItem(AbstractEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    HBox vb = createResultRow(item);
                    setGraphic(vb);
                }
            }
        });
        VBox.setVgrow(result, Priority.ALWAYS);
        result.setOnMouseClicked(this::handleMouseEvent);
        layout.getChildren().add(result);
    }

    private HBox createResultRow(AbstractEntry item) {
        HBox vb = new HBox();
        vb.setAlignment(Pos.CENTER_LEFT);
        String nameOfEntry = item.getEntryProperties().getAbsolutePath();
        Label nameLabel = new Label(nameOfEntry);
        ImageView iv = new ImageView(IconManager.getFileIcon(item.getEntryProperties().getAbsolutePath()));
        HBox.setMargin(iv, new Insets(0, 3, 0, 0));
        vb.getChildren().addAll(iv, nameLabel);
        return vb;
    }

    private void handleMouseEvent(MouseEvent event) {
        if (MouseButton.PRIMARY.equals(event.getButton())) {
            AbstractEntry selectedItem = result.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && selectedItem != null) {
                handlePrimaryMouseButtonDoubleClick(selectedItem);
            }
        }
    }

    private void handlePrimaryMouseButtonDoubleClick(AbstractEntry selectedItem) {
        if (Desktop.isDesktopSupported()) {
            File target = selectedItem.getFilesystemEntry();
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(target.toURI());
                } catch (IOException exception) {
                    log.error("Unable to open selected file", exception);
                }
            }).start();
        }
    }

    private void createPathSelection() {
        createPathSelectionWrapper();
        createPathLabelAndInput();
        createRecursiveLabelAndCheck();

        pathSelectionWrapper.getChildren().addAll(pathLabel, pathComboBox, recursiveLabel, recursive);
        layout.getChildren().add(pathSelectionWrapper);
    }

    private void createPathSelectionWrapper() {
        pathSelectionWrapper = new HBox(10);
        pathSelectionWrapper.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pathSelectionWrapper, Priority.ALWAYS);
    }

    private void createRecursiveLabelAndCheck() {
        recursive = new CheckBox();
        recursiveLabel = new Label(getString("recursive-label") + "?");
    }

    private void createPathLabelAndInput() {
        pathLabel = new Label(getString("path") + ":");
        pathComboBox = new PathComboBox();
        pathComboBox.setMinWidth(500);
    }


    private void search() {
        layout.setCursor(Cursor.WAIT);
        final List<AbstractEntry> files = Collections.synchronizedList(new ArrayList<>());
        try {
            if (recursive.isSelected()) {
                Files.find(Paths.get(pathComboBox.getSelectedPath()),
                        Integer.MAX_VALUE,
                        filesMatchingInput())
                        .parallel()
                        .forEach(path -> files.add(createEntryFromPath(path)));
            } else {
                File[] filesArray = getFiles();
                Arrays.asList(filesArray).parallelStream().filter(
                        file -> file.getName().contains(input.getText()))
                        .forEach(file -> files.add(createEntryFromFile(file)));
            }
            result.getItems().setAll(files);
        } catch (IOException exception) {
            log.error("Exception during search file", exception);
        }
        layout.setCursor(Cursor.DEFAULT);
    }

    private BiPredicate<Path, BasicFileAttributes> filesMatchingInput() {
        return (filePath, fileAttr) -> fileNameContainsInputText(filePath);
    }

    private boolean fileNameContainsInputText(Path filePath) {
        return filePath.toFile().getName().contains(input.getText());
    }

    private File[] getFiles() {
        File[] filesArray = new File(pathComboBox.getSelectedPath()).listFiles();
        if (filesArray == null) {
            filesArray = FileUtils.EMPTY_FILE_ARRAY;
        }
        return filesArray;
    }

    private AbstractEntry createEntryFromFile(File file) {
        return file.isFile()
                ? new FileEntry(file.getAbsolutePath())
                : new DirectoryEntry(file.getAbsolutePath());
    }

    private AbstractEntry createEntryFromPath(Path path) {
        return createEntryFromFile(path.toFile());
    }

}
