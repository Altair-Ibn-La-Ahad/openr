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
            MainWindow.getStatusBar().managedProperty().setValue(newValue);
            MainWindow.getStatusBar().setVisible(newValue);
        });

        CheckMenuItem leftPanelVisibility = new CheckMenuItem(getString("left-panel"));
        leftPanelVisibility.setSelected(true);
        leftPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainWindow.getLeftWrapper().visibleProperty().setValue(newValue);
            MainWindow.getLeftWrapper().managedProperty().setValue(newValue);
        });

        CheckMenuItem rightPanelVisibility = new CheckMenuItem(getString("right-panel"));
        rightPanelVisibility.setSelected(true);
        rightPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainWindow.getRightWrapper().visibleProperty().setValue(newValue);
            MainWindow.getRightWrapper().managedProperty().setValue(newValue);
        });

        leftPanelVisibility.disableProperty().bind(rightPanelVisibility.selectedProperty().not());
        rightPanelVisibility.disableProperty().bind(leftPanelVisibility.selectedProperty().not());

        super.getItems().addAll(statusBarVisibility, leftPanelVisibility, rightPanelVisibility);
    }
}
