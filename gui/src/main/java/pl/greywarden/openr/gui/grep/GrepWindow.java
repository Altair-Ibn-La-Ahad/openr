package pl.greywarden.openr.gui.grep;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.unix4j.Unix4j;
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
import java.util.stream.Collectors;

@Log4j
public class GrepWindow extends Stage {

    private TextField regexInput;
    private Button doGrep;
    private TableView<GrepResult> resultTableView;
    private VBox layout;
    private CheckBox recursive;
    private ComboBox<String> pathComboBox;

    private I18nManager i18n;

    private final DirectoryView left;
    private final DirectoryView right;

    public GrepWindow(DirectoryView left, DirectoryView right) {
        super();

        this.left = left;
        this.right = right;

        i18n = I18nManager.getInstance();
        i18n.setBundle("grep-window");
        setTitle(i18n.getString("title"));

        layout = new VBox(5);
        layout.setPadding(new Insets(5));

        createSearchBar();
        createPathSelection();
        createResultListView();

        super.setScene(new Scene(layout));
        super.centerOnScreen();
        super.setMaximized(true);
        super.show();
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

    private void createSearchBar() {
        HBox wrapper = new HBox(5);

        regexInput = new TextField();
        doGrep = new Button("->");
        doGrep.setOnAction(handleGrep());

        regexInput.setOnKeyPressed(event -> {
            if (regexInput.textProperty().isNotEmpty().get() && event.getCode().equals(KeyCode.ENTER)) {
                String regex = regexInput.getText();
                List<File> files = getFilesToGrep();
                resultTableView.getItems().clear();
                files.forEach(file -> {
                    String result = Unix4j.grep(regex, file).toStringResult();
                    if (!result.isEmpty()) {
                        resultTableView.getItems().add(new GrepResult(file, result));
                    }
                });
            }
        });

        HBox.setHgrow(regexInput, Priority.ALWAYS);
        doGrep.disableProperty().bind(regexInput.textProperty().isEmpty());

        wrapper.getChildren().addAll(regexInput, doGrep);
        layout.getChildren().add(wrapper);
    }

    @SuppressWarnings("unchecked")
    private void createResultListView() {
        resultTableView = new TableView<>();
        resultTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        resultTableView.setPlaceholder(new Label(""));
        TableColumn <GrepResult, String> text = new TableColumn(i18n.getString("text"));
        TableColumn <GrepResult, String> pathToFile = new TableColumn(i18n.getString("path"));
        text.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getText()));
        pathToFile.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFile().getAbsolutePath()));
        resultTableView.getColumns().addAll(text, pathToFile);
        resultTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox.setVgrow(resultTableView, Priority.ALWAYS);
        layout.getChildren().add(resultTableView);
    }

    private EventHandler<ActionEvent> handleGrep() {
        return event -> {
            String regex = regexInput.getText();
            List<File> files = getFilesToGrep();
            resultTableView.getItems().clear();
            files.forEach(file -> {
                String result = Unix4j.grep(regex, file).toStringResult();
                if (!result.isEmpty()) {
                    resultTableView.getItems().add(new GrepResult(file, result));
                }
            });
        };
    }

    private List<File> getFilesToGrep() {
        final List<File> files = new ArrayList<>();
        try {
            if (recursive.isSelected()) {
                Files.find(Paths.get(pathComboBox.getSelectionModel().getSelectedItem()),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr) -> fileAttr.isRegularFile())
                        .forEach(path -> files.add(path.toFile()));
            } else {
                File[] filesToGrep = new File(pathComboBox.getSelectionModel().getSelectedItem()).listFiles();
                if (filesToGrep == null) {
                    filesToGrep = FileUtils.EMPTY_FILE_ARRAY;
                }
                files.addAll(Arrays.stream(filesToGrep).filter(File::isFile).collect(Collectors.toList()));
            }
            return files;
        } catch (IOException exception) {
            log.error("Error during fetching files for grep", exception);
            return Collections.emptyList();
        }
    }

}
