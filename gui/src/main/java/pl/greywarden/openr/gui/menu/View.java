package pl.greywarden.openr.gui.menu;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class View extends Menu {

    private final CheckMenuItem leftPanelVisibility = createLeftPanelVisibilityCheck();
    private final CheckMenuItem rightPanelVisibility = createRightPanelVisibilityCheck();

    public View() {
        super(getString("view-menu"));
        forceOnePanelAlwaysVisible();
        CheckMenuItem statusBarVisibility = createStatusBarVisibilityCheck();
        CheckMenuItem toolBarVisibility = createToolBarVisibilityCheck();
        super.getItems().addAll(statusBarVisibility, toolBarVisibility, new SeparatorMenuItem(),
                leftPanelVisibility, rightPanelVisibility);
    }

    private void forceOnePanelAlwaysVisible() {
        leftPanelVisibility.disableProperty().bind(rightPanelVisibility.selectedProperty().not());
        rightPanelVisibility.disableProperty().bind(leftPanelVisibility.selectedProperty().not());
    }

    private CheckMenuItem createRightPanelVisibilityCheck() {
        CheckMenuItem rightPanelVisibility = new CheckMenuItem(getString("right-panel"));
        rightPanelVisibility.setSelected(true);
        rightPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getRightWrapper().managedProperty().setValue(newValue));
        return rightPanelVisibility;
    }

    private CheckMenuItem createLeftPanelVisibilityCheck() {
        CheckMenuItem leftPanelVisibility = new CheckMenuItem(getString("left-panel"));
        leftPanelVisibility.setSelected(true);
        leftPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getLeftWrapper().managedProperty().setValue(newValue));
        return leftPanelVisibility;
    }

    private CheckMenuItem createStatusBarVisibilityCheck() {
        CheckMenuItem statusBarVisibility = new CheckMenuItem(getString("status-bar-visibility-check"));
        statusBarVisibility.setSelected(true);
        statusBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getStatusBar().managedProperty().setValue(newValue));
        return statusBarVisibility;
    }

    private CheckMenuItem createToolBarVisibilityCheck() {
        CheckMenuItem toolBarVisibility = new CheckMenuItem(getString("tool-bar"));
        toolBarVisibility.setSelected(true);
        toolBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getMainWindowToolBar().managedProperty().setValue(newValue)
        );
        return toolBarVisibility;
    }
}
