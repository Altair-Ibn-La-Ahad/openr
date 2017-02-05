package pl.greywarden.openr.gui.grep;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.StatusBar;
import org.unix4j.Unix4j;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.dialogs.PathComboBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class GrepWindow extends Stage {

    private TextField regexInput;
    private TableView<GrepResult> resultTableView;
    private final VBox layout;
    private CheckBox recursive;
    private PathComboBox pathComboBox;
    private ProgressBar progressBar = new ProgressBar(0.0);

    public GrepWindow() {
        super();
        setTitle(getString("grep-window-title"));

        layout = new VBox(5);
        layout.setPadding(new Insets(5));

        StatusBar statusBar = new StatusBar();
        statusBar.setText(getString("grep-window-title"));
        statusBar.getRightItems().setAll(progressBar);

        createSearchBar();
        createPathSelection();
        createResultListView();
        layout.getChildren().add(statusBar);

        layout.setOnKeyPressed(event -> {
            if (KeyCode.ESCAPE.equals(event.getCode())) {
                super.close();
            }
        });
        resultTableView.setOnKeyPressed(event -> {
            if (KeyCode.ESCAPE.equals(event.getCode())) {
                super.close();
            }
        });

        super.setScene(new Scene(layout));
        super.centerOnScreen();
        super.show();
    }

    private void createPathSelection() {
        GridPane wrapper = new GridPane();
        wrapper.setHgap(10);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        Label pathLabel = new Label(getString("path") + ":");
        pathComboBox = new PathComboBox();
        pathComboBox.setMinWidth(500);
        recursive = new CheckBox();
        Label recursiveLabel = new Label(getString("recursive-label") + "?");
        wrapper.addRow(0, pathLabel, pathComboBox, recursiveLabel, recursive);
        layout.getChildren().add(wrapper);
    }

    private void createSearchBar() {
        HBox wrapper = new HBox(5);

        regexInput = new TextField();
        Button doGrep = new Button();
        doGrep.setGraphic(IconManager.getIcon("go"));
        doGrep.setOnAction(handleGrep());

        regexInput.setOnKeyPressed(event -> {
            if (regexInput.textProperty().isNotEmpty().get()
                    && event.getCode().equals(KeyCode.ENTER)) {
                Task<Double> grep = grep();
                progressBar.progressProperty().bind(grep.progressProperty());
                Thread thread = new Thread(grep);
                thread.setDaemon(true);
                thread.start();
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
        TableColumn<GrepResult, String> text = new TableColumn(getString("grep-result-text"));
        TableColumn<GrepResult, String> pathToFile = new TableColumn(getString("grep-result-path"));
        text.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getText()));
        pathToFile.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFile().getAbsolutePath()));
        resultTableView.getColumns().addAll(text, pathToFile);
        resultTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox.setVgrow(resultTableView, Priority.ALWAYS);
        layout.getChildren().add(resultTableView);
    }

    private EventHandler<ActionEvent> handleGrep() {
        return event -> new Thread(this::grep).start();
    }

    private Task<Double> grep() {
        return new Task<Double>() {
            @Override
            protected Double call() throws Exception {
                final String regex = regexInput.getText();
                List<File> filesToGrep = Collections.synchronizedList(getFilesToGrep());
                List<GrepResult> grepResults = Collections.synchronizedList(new LinkedList<>());
                final AtomicInteger steps = new AtomicInteger(0);
                filesToGrep.parallelStream().forEach(file -> {
                    String result = Unix4j.grep(regex, file).toStringResult();
                    if (!result.isEmpty()) {
                        grepResults.add(new GrepResult(file, result));
                    }
                    updateProgress(steps.incrementAndGet(), filesToGrep.size());
                });
                resultTableView.getItems().setAll(grepResults);
                return getProgress();
            }
        };
    }

    private List<File> getFilesToGrep() {
        final List<File> files = new LinkedList<>();
        try {
            if (recursive.isSelected()) {
                Files.find(Paths.get(pathComboBox.getSelectedPath()),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr) -> fileAttr.isRegularFile())
                        .parallel()
                        .forEach(path -> files.add(path.toFile()));
            } else {
                File[] filesToGrep = new File(pathComboBox.getSelectedPath()).listFiles();
                if (filesToGrep == null) {
                    filesToGrep = FileUtils.EMPTY_FILE_ARRAY;
                }
                files.addAll(Arrays.asList(filesToGrep).parallelStream()
                        .filter(File::isFile).collect(Collectors.toList()));
            }
            return Collections.synchronizedList(files);
        } catch (IOException exception) {
            log.error("Error during fetching files for grep", exception);
            return Collections.emptyList();
        }
    }

}
