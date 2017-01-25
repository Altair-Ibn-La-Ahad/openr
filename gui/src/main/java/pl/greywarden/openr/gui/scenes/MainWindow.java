package pl.greywarden.openr.gui.scenes;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.gui.IconManager;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.dialogs.ConfirmExitDialog;
import pl.greywarden.openr.gui.dialogs.NewFileDialog;
import pl.greywarden.openr.gui.find.FindWindow;
import pl.greywarden.openr.gui.grep.GrepWindow;
import pl.greywarden.openr.i18n.I18nManager;

import java.util.Optional;

@Log4j
public class MainWindow extends Stage {

    private final I18nManager i18n = I18nManager.getInstance();

    private CentralContainter centralContainter;
    private MainWindowStatusBar statusBar;

    private final VBox layout = new VBox();

    public MainWindow() {
        Scene scene = new Scene(layout);
        buildScene();

        super.setTitle("OpenR " + AboutDialog.getVersion());
        super.setScene(scene);
        super.setMaximized(true);

        super.setOnCloseRequest(event -> {
            Optional<ButtonType> confirm = new ConfirmExitDialog().showAndWait();
            if (confirm.isPresent()) {
                if (ButtonBar.ButtonData.YES.equals(confirm.get().getButtonData())) {
                    Platform.exit();
                }
            }
            event.consume();
        });
    }

    private void buildScene() {
        createCentralContainer();
        createMenuBar();
        createToolBar();
        createStatusBar();
    }

    private void createMenuBar() {
        i18n.setBundle("menu-bar");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                createFileMenu(),
                createViewMenu(),
                createHelpMenu());

        layout.getChildren().add(0, menuBar);
    }

    private Menu createFileMenu() {
        Menu file = new Menu(i18n.getString("file"));
        file.getItems().add(new NewFileMenu(
                centralContainter.getLeftView().getDirectoryView(),
                centralContainter.getRightView().getDirectoryView()));
        file.getItems().add(new NewDocumentMenu(
                centralContainter.getLeftView().getDirectoryView(),
                centralContainter.getRightView().getDirectoryView()));
        file.getItems().add(createSettingsMenuItem());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(createExitMenuItem());

        return file;
    }

    private MenuItem createSettingsMenuItem() {
        MenuItem settings = new MenuItem(i18n.getString("settings"));
        settings.setGraphic(IconManager.getIcon("settings"));
        settings.setOnAction(event -> System.err.println("Settings window placeholder"));
        settings.setAccelerator(new KeyCodeCombination(
                KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        return settings;
    }

    private MenuItem createExitMenuItem() {
        MenuItem exit = new MenuItem(i18n.getString("exit"));
        exit.setGraphic(IconManager.getIcon("exit"));
        exit.setOnAction(event -> super.fireEvent(
                new WindowEvent(this.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));
        exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        return exit;
    }

    private void createCentralContainer() {
        centralContainter = new CentralContainter();
        VBox.setVgrow(centralContainter, Priority.ALWAYS);
        layout.getChildren().add(centralContainter);
    }

    private void createStatusBar() {
        statusBar = new MainWindowStatusBar();
        layout.getChildren().add(statusBar);
    }

    private Menu createViewMenu() {
        Menu view = new Menu(i18n.getString("view"));

        CheckMenuItem statusBarVisibility = new CheckMenuItem(i18n.getString("status-bar"));
        statusBarVisibility.setSelected(true);
        statusBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            statusBar.managedProperty().setValue(newValue);
            statusBar.setVisible(newValue);
        });
        view.getItems().addAll(statusBarVisibility);
        return view;
    }

    private Menu createHelpMenu() {
        Menu help = new Menu(i18n.getString("help"));

        MenuItem about = new MenuItem(i18n.getString("about"));
        about.setOnAction(event -> new AboutDialog().show());

        help.getItems().addAll(about);
        return help;
    }

    private void createToolBar() {
        i18n.setBundle("tool-bar");
        ToolBar toolBar = new ToolBar();

        Button newFile = new Button();
        newFile.setGraphic(IconManager.getIcon("new-file"));
        newFile.setOnAction(event -> new NewFileDialog(
                centralContainter.getLeftView().getDirectoryView(),
                centralContainter.getRightView().getDirectoryView()));

        Button grep = new Button();
        grep.setGraphic(IconManager.getIcon("grep"));
        grep.setOnAction(event -> new GrepWindow(
                centralContainter.getLeftView().getDirectoryView(),
                centralContainter.getRightView().getDirectoryView()));

        Button find = new Button();
        find.setGraphic(IconManager.getIcon("find"));
        find.setOnAction(event -> new FindWindow(centralContainter.getLeftView().getDirectoryView(),
                centralContainter.getRightView().getDirectoryView()));

        toolBar.getItems().addAll(newFile, new Separator(), grep, find);
        layout.getChildren().add(1, toolBar);
    }
}
