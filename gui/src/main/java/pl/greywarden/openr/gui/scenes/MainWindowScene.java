package pl.greywarden.openr.gui.scenes;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class MainWindowScene extends VBox {

    private final I18nManager i18n = I18nManager.getInstance();

    private CentralContainter centralContainter = new CentralContainter();

    public MainWindowScene() {
        createMenuBar();
        createCentralContainer();
        createStatusBar();
    }

    private void createMenuBar() {
        i18n.setBundle("menu-bar");

        Menu file = new Menu(i18n.getString("file"));
        file.getItems().add(createNewFileMenu());
        file.getItems().add(createSettingsMenuItem());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(createExitMenuItem());

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file);

        super.getChildren().addAll(menuBar);
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
        ImageView icon = new ImageView(new Image(MainWindowScene.class.getClassLoader()
                .getResourceAsStream("icons/settings.png")));
        settings.setGraphic(icon);
        settings.setOnAction(event -> System.err.println("Settings window placeholder"));
        return settings;
    }

    private MenuItem createExitMenuItem() {
        MenuItem exit = new MenuItem(i18n.getString("exit"));
        ImageView icon = new ImageView(new Image(MainWindowScene.class.getClassLoader()
                .getResourceAsStream("icons/exit.png")));
        exit.setGraphic(icon);
        exit.setOnAction(event -> super.fireEvent(
                new WindowEvent(this.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));
        return exit;
    }

    private void createCentralContainer() {
        VBox.setVgrow(centralContainter, Priority.ALWAYS);
        super.getChildren().add(centralContainter);
    }

    private void createStatusBar() {
        MainWindowStatusBar statusBar = new MainWindowStatusBar();
        super.getChildren().add(statusBar);
    }
}
