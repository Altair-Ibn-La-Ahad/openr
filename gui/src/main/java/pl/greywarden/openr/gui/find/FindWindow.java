package pl.greywarden.openr.gui.find;

import javafx.application.Platform;
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
import javafx.scene.input.MouseButton;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class FindWindow extends Stage {

    private final VBox layout = new VBox(5);
    private ListView<AbstractEntry> result;
    private TextField input;
    private PathComboBox pathComboBox;
    private CheckBox recursive;

    public FindWindow() {
        super();
        layout.setPadding(new Insets(5));

        createSearchBar();
        createPathSelection();
        createResultList();

        super.setTitle(getString("find-file"));
        super.setScene(new Scene(layout));
        show();
    }

    private void createSearchBar() {
        HBox wrapper = new HBox(5);

        input = new TextField();
        Button doSearch = new Button();
        doSearch.setGraphic(IconManager.getIcon("go"));
        doSearch.setOnAction(event -> Platform.runLater(this::search));

        input.setOnKeyPressed(event -> {
            if (input.textProperty().isNotEmpty().get() && event.getCode().equals(KeyCode.ENTER)) {
                Platform.runLater(this::search);
            }
        });

        HBox.setHgrow(input, Priority.ALWAYS);
        doSearch.disableProperty().bind(input.textProperty().isEmpty());

        wrapper.getChildren().addAll(input, doSearch);
        layout.getChildren().add(wrapper);
    }

    private void createResultList() {
        result = new ListView<>();
        result.setCellFactory(param -> new ListCell<AbstractEntry>() {
            @Override
            protected void updateItem(AbstractEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    HBox vb = new HBox();
                    vb.setAlignment(Pos.CENTER_LEFT);
                    String nameOfEntry = item.getEntryProperties().getAbsolutePath();
                    Label nameLabel = new Label(nameOfEntry);
                    ImageView iv = new ImageView(IconManager.getFileIcon(item.getEntryProperties().getAbsolutePath()));
                    HBox.setMargin(iv, new Insets(0, 3, 0, 0));
                    vb.getChildren().addAll(iv, nameLabel);
                    setGraphic(vb);
                }
            }
        });
        VBox.setVgrow(result, Priority.ALWAYS);
        result.setOnMouseClicked(event -> {
            if (MouseButton.PRIMARY.equals(event.getButton())) {
                AbstractEntry selectedItem = result.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2 && selectedItem != null) {
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
            }
        });
        layout.getChildren().add(result);
    }

    private void createPathSelection() {
        HBox wrapper = new HBox(10);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        Label pathLabel = new Label(getString("path") + ":");
        pathComboBox = new PathComboBox();
        pathComboBox.setMinWidth(500);
        recursive = new CheckBox();
        Label recursiveLabel = new Label(getString("recursive-label") + "?");
        HBox.setHgrow(wrapper, Priority.ALWAYS);

        wrapper.getChildren().addAll(pathLabel, pathComboBox, recursiveLabel, recursive);
        layout.getChildren().add(wrapper);
    }


    private void search() {
        layout.setCursor(Cursor.WAIT);
        final List<AbstractEntry> files = Collections.synchronizedList(new ArrayList<>());
        try {
            if (recursive.isSelected()) {
                Files.find(Paths.get(pathComboBox.getSelectedPath()),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr) ->
                                filePath.toFile().getName().contains(input.getText()))
                        .parallel()
                        .forEach(path -> files.add(path.toFile().isFile()
                                ? new FileEntry(path.toFile().getAbsolutePath())
                                : new DirectoryEntry(path.toFile().getAbsolutePath())));
            } else {
                File[] filesArray = new File(pathComboBox.getSelectedPath()).listFiles();
                if (filesArray == null) {
                    filesArray = FileUtils.EMPTY_FILE_ARRAY;
                }
                Arrays.asList(filesArray).parallelStream().filter(
                        file -> file.getName().contains(input.getText()))
                        .forEach(file -> files.add(file.isFile()
                                ? new FileEntry(file.getAbsolutePath())
                                : new DirectoryEntry(file.getAbsolutePath())));
            }
            result.getItems().setAll(files);
        } catch (IOException exception) {
            log.error("Exception during search file", exception);
        }
        layout.setCursor(Cursor.DEFAULT);
    }

}
