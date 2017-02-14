package pl.greywarden.openr.gui.menu;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class View extends Menu {

    private final CheckMenuItem leftPanelVisibility = createLeftPanelVisibilityCheck();
    private final CheckMenuItem rightPanelVisibility = createRightPanelVisibilityCheck();
    private final CheckMenuItem statusBarVisibility = createStatusBarVisibilityCheck();
    private final CheckMenuItem toolBarVisibility = createToolBarVisibilityCheck();
    private final CheckMenuItem hiddenFilesVisibility = createHiddenFilesVisibilityCheck();

    private static View instance;

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }
        instance.setLabels();
        return instance;
    }

    private View() {
        super();
        forceOnePanelAlwaysVisible();
        super.getItems().setAll(statusBarVisibility, toolBarVisibility, new SeparatorMenuItem(),
                leftPanelVisibility, rightPanelVisibility, new SeparatorMenuItem(),
                hiddenFilesVisibility);
    }

    public BooleanProperty hiddenFilesVisible() {
        return hiddenFilesVisibility.selectedProperty();
    }

    private void setLabels() {
        super.setText(getString("view-menu"));
        hiddenFilesVisibility.setText(getString("hidden-files"));
        rightPanelVisibility.setText(getString("right-panel"));
        leftPanelVisibility.setText(getString("left-panel"));
        toolBarVisibility.setText(getString("tool-bar"));
        statusBarVisibility.setText(getString("status-bar-visibility-check"));
    }

    private CheckMenuItem createHiddenFilesVisibilityCheck() {
        CheckMenuItem hiddenFiles = new CheckMenuItem();
        hiddenFiles.setSelected(Boolean.valueOf(getSetting(Setting.HIDDEN_FILES_VISIBLE)));
        hiddenFiles.selectedProperty().addListener((observable, oldValue, newValue) -> {
            DirectoryView.showHiddenFiles = newValue;
            ConfigManager.setProperty(Setting.HIDDEN_FILES_VISIBLE, newValue);
            MainWindow.reloadViews();
        });
        return hiddenFiles;
    }

    private CheckMenuItem createRightPanelVisibilityCheck() {
        CheckMenuItem rightPanelVisibility = new CheckMenuItem();
        rightPanelVisibility.setSelected(Boolean.valueOf(getSetting(Setting.RIGHT_VIEW_VISIBLE)));
        rightPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getRightWrapper().managedProperty().setValue(newValue));
        return rightPanelVisibility;
    }

    private CheckMenuItem createLeftPanelVisibilityCheck() {
        CheckMenuItem leftPanelVisibility = new CheckMenuItem();
        leftPanelVisibility.setSelected(Boolean.valueOf(getSetting(Setting.LEFT_VIEW_VISIBLE)));
        leftPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getLeftWrapper().managedProperty().setValue(newValue));
        return leftPanelVisibility;
    }

    private CheckMenuItem createStatusBarVisibilityCheck() {
        CheckMenuItem statusBarVisibility = new CheckMenuItem();
        statusBarVisibility.setSelected(Boolean.valueOf(getSetting(Setting.STATUS_BAR_VISIBLE)));
        statusBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getStatusBar().managedProperty().setValue(newValue));
        return statusBarVisibility;
    }

    private CheckMenuItem createToolBarVisibilityCheck() {
        CheckMenuItem toolBarVisibility = new CheckMenuItem();
        toolBarVisibility.setSelected(Boolean.valueOf(getSetting(Setting.TOOL_BAR_VISIBLE)));
        toolBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) ->
                MainWindow.getMainWindowToolBar().managedProperty().setValue(newValue));
        return toolBarVisibility;
    }

    private void forceOnePanelAlwaysVisible() {
        leftPanelVisibility.disableProperty().bind(rightPanelVisibility.selectedProperty().not());
        rightPanelVisibility.disableProperty().bind(leftPanelVisibility.selectedProperty().not());
    }
}
