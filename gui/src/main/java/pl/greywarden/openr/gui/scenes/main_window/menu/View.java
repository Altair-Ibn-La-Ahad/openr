package pl.greywarden.openr.gui.scenes.main_window.menu;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class View extends Menu {

    public View() {
        super(getString("view-menu"));
        CheckMenuItem statusBarVisibility = new CheckMenuItem(getString("status-bar-visibility-check"));
        statusBarVisibility.setSelected(true);
        statusBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainWindow.getInstance();
            MainWindow.getStatusBar().managedProperty().setValue(newValue);
            MainWindow.getStatusBar().setVisible(newValue);
        });
        super.getItems().addAll(statusBarVisibility);
    }
}
