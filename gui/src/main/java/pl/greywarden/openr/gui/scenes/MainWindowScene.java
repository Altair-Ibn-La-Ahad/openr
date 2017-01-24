package pl.greywarden.openr.gui.scenes;

import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.gui.IconManager;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.gui.dialogs.NewFileDialog;
import pl.greywarden.openr.gui.grep.GrepWindow;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class MainWindowScene extends VBox {

    private final I18nManager i18n = I18nManager.getInstance();

    private final CentralContainter centralContainter = new CentralContainter();
    private final MainWindowStatusBar statusBar = new MainWindowStatusBar();

    public MainWindowScene() {
        i18n.setBundle("menu-bar");
        createMenuBar();

        i18n.setBundle("tool-bar");
        createToolBar();
        createCentralContainer();
        createStatusBar();
    }

    private void createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                createFileMenu(),
                createViewMenu(),
                createHelpMenu());

        super.getChildren().addAll(menuBar);
    }

    private Menu createFileMenu() {
        Menu file = new Menu(i18n.getString("file"));
        file.getItems().add(createNewFileMenu());
        file.getItems().add(createSettingsMenuItem());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(createExitMenuItem());

        return file;
    }

    private Menu createNewFileMenu() {
        Menu menu = new Menu(i18n.getString("new-file"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().forEach(template -> {
            MenuItem item = new MenuItem(i18n.getString(template.getName()));
            item.setOnAction(event ->
                    new CreateFileDialog(template,
                            centralContainter.getLeftView().getDirectoryView(),
                            centralContainter.getRightView().getDirectoryView()));
            items.add(item);
        });
        menu.getItems().addAll(items);
        return menu;
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
        VBox.setVgrow(centralContainter, Priority.ALWAYS);
        super.getChildren().add(centralContainter);
    }

    private void createStatusBar() {
        super.getChildren().add(statusBar);
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

        toolBar.getItems().addAll(newFile, grep);
        super.getChildren().add(toolBar);
    }
}
