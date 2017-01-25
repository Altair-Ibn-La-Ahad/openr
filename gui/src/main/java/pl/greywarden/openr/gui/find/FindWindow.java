package pl.greywarden.openr.gui.find;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.gui.IconManager;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Log4j
public class FindWindow extends Stage {

    private final VBox layout = new VBox(5);
    private final I18nManager i18n = I18nManager.getInstance();
    private ListView<AbstractEntry> result;
    private TextField input;
    private ComboBox<String> pathComboBox;
    private CheckBox recursive;

    private final DirectoryView left;
    private final DirectoryView right;

    public FindWindow(DirectoryView left, DirectoryView right) {
        super();
        this.left = left;
        this.right = right;
        i18n.setBundle("find-window");
        layout.setPadding(new Insets(5));

        createSearchBar();
        createPathSelection();
        createResultList();

        super.setScene(new Scene(layout));
        show();
    }

    private void createSearchBar() {
        HBox wrapper = new HBox(5);

        input = new TextField();
        Button doSearch = new Button();
        doSearch.setGraphic(IconManager.getIcon("go"));
        doSearch.setOnAction(event -> search());

        input.setOnKeyPressed(event -> {
            if (input.textProperty().isNotEmpty().get() && event.getCode().equals(KeyCode.ENTER)) {
                search();
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
        layout.getChildren().add(result);
    }

    private void createPathSelection() {
        HBox wrapper = new HBox(10);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        Label pathLabel = new Label(i18n.getString("path") + ":");
        pathComboBox = new ComboBox<>();
        pathComboBox.getItems().addAll(left.getRootPath(), right.getRootPath());
        pathComboBox.getSelectionModel().select(0);
        pathComboBox.setMinWidth(500);
        recursive = new CheckBox();
        Label recursiveLabel = new Label(i18n.getString("recursive"));
        HBox.setHgrow(wrapper, Priority.ALWAYS);

        wrapper.getChildren().addAll(pathLabel, pathComboBox, recursiveLabel, recursive);
        layout.getChildren().add(wrapper);
    }


    private void search() {
        final List<AbstractEntry> files = Collections.synchronizedList(new ArrayList<>());
        try {
            if (recursive.isSelected()) {
                Files.find(Paths.get(pathComboBox.getSelectionModel().getSelectedItem()),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr) ->
                                filePath.toFile().getName().contains(input.getText()))
                        .parallel()
                        .forEach(path -> files.add(path.toFile().isFile()
                                ? new FileEntry(path.toFile().getAbsolutePath())
                                : new DirectoryEntry(path.toFile().getAbsolutePath())));
            } else {
                File[] filesArray = new File(pathComboBox.getSelectionModel().getSelectedItem()).listFiles();
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
    }

}
