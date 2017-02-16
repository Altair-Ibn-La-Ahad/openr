package pl.greywarden.openr.gui.dialogs.grep;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import pl.greywarden.openr.commons.PathComboBox;

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
    private GrepResultTableView grepResultTableView;
    private VBox layout;
    private CheckBox recursive;
    private PathComboBox pathComboBox;
    private final ProgressBar progressBar = new ProgressBar(0.0);
    private Button doGrep;
    private Label recursiveLabel;
    private Label pathLabel;

    public GrepWindow() {
        super();
        setTitle(getString("grep-window-title"));

        createWindowLayout();

        createSearchBar();
        createPathSelection();
        createResultTableView();
        createStatusBar();

        grepResultTableView.setOnKeyPressed(this::closeWindowOnEscapeKey);

        super.setScene(new Scene(layout));
        super.centerOnScreen();
        super.show();
    }

    private void createWindowLayout() {
        layout = new VBox(5);
        layout.setPadding(new Insets(5));
        layout.setOnKeyPressed(this::closeWindowOnEscapeKey);
        layout.setMinWidth(600);
    }

    private void closeWindowOnEscapeKey(KeyEvent event) {
        if (KeyCode.ESCAPE.equals(event.getCode())) {
            super.close();
        }
    }

    private void createStatusBar() {
        StatusBar statusBar = new StatusBar();
        statusBar.setText(getString("grep-window-title"));
        statusBar.getRightItems().setAll(progressBar);
        layout.getChildren().add(statusBar);
    }

    private void createPathSelection() {
        GridPane wrapper = createPathSelectionWrapper();
        createPathLabelAndInput();
        createRecursiveLabelAndCheck();
        wrapper.addRow(0, pathLabel, pathComboBox, recursiveLabel, recursive);
        layout.getChildren().add(wrapper);
    }

    private GridPane createPathSelectionWrapper() {
        GridPane wrapper = new GridPane();
        wrapper.setHgap(10);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        return wrapper;
    }

    private void createPathLabelAndInput() {
        pathLabel = new Label(getString("path") + ":");
        pathComboBox = new PathComboBox();
        pathComboBox.setMinWidth(500);
    }

    private void createRecursiveLabelAndCheck() {
        recursive = new CheckBox();
        recursiveLabel = new Label(getString("recursive-label") + "?");
    }

    private void createSearchBar() {
        HBox wrapper = new HBox(5);

        createRegexInput();
        createGrepButton();

        HBox.setHgrow(regexInput, Priority.ALWAYS);
        doGrep.disableProperty().bind(regexInput.textProperty().isEmpty());

        wrapper.getChildren().addAll(regexInput, doGrep);
        layout.getChildren().add(wrapper);
    }

    private void createRegexInput() {
        regexInput = new TextField();
        regexInput.setOnKeyPressed(this::handleRegexInputKeyEvent);
    }

    private void createGrepButton() {
        doGrep = new Button();
        doGrep.setGraphic(IconManager.getProgramIcon("go"));
        doGrep.setOnAction(handleGrep());
    }

    private void handleRegexInputKeyEvent(KeyEvent event) {
        if (regexInput.textProperty().isNotEmpty().get()
                && event.getCode().equals(KeyCode.ENTER)) {
            Task<Double> grep = grep();
            progressBar.progressProperty().bind(grep.progressProperty());
            Thread thread = new Thread(grep);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @SuppressWarnings("unchecked")
    private void createResultTableView() {
        grepResultTableView = new GrepResultTableView();
        VBox.setVgrow(grepResultTableView, Priority.ALWAYS);
        layout.getChildren().add(grepResultTableView);
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
                grepResultTableView.getItems().setAll(grepResults);
                return getProgress();
            }
        };
    }

    private List<File> getFilesToGrep() {
        final List<File> files = Collections.synchronizedList(new LinkedList<>());
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