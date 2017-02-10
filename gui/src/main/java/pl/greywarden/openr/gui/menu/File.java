package pl.greywarden.openr.gui.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.WindowEvent;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.settings.Settings;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class File extends Menu {

    public File() {
        super(getString("file-menu"));
        super.getItems().addAll(
                new NewFileMenu(),
                new NewDocumentMenu(),
                createSettingsMenuItem(),
                new SeparatorMenuItem(),
                createExitMenuItem());
    }

    private MenuItem createSettingsMenuItem() {
        MenuItem settings = new MenuItem(getString("settings-menu-item"));
        settings.setGraphic(IconManager.getIcon("settings"));
        settings.setOnAction(event -> new Settings());
        settings.setAccelerator(new KeyCodeCombination(
                KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        return settings;
    }

    private MenuItem createExitMenuItem() {
        MenuItem exit = new MenuItem(getString("exit-menu-item"));
        exit.setGraphic(IconManager.getIcon("exit"));
        exit.setOnAction(event -> fireClosingEvent());
        exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        return exit;
    }

    private void fireClosingEvent() {
        MainWindow.getInstance().fireEvent(
                new WindowEvent(MainWindow.getInstance().getScene().getWindow(),
                        WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
