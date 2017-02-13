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

    private static final CheckMenuItem leftPanelVisibility = createLeftPanelVisibilityCheck();
    private static final CheckMenuItem rightPanelVisibility = createRightPanelVisibilityCheck();
    private static final CheckMenuItem statusBarVisibility = createStatusBarVisibilityCheck();
    private static final CheckMenuItem toolBarVisibility = createToolBarVisibilityCheck();
    private static final CheckMenuItem hiddenFilesVisibility = createHiddenFilesVisibilityCheck();

    public View() {
        super(getString("view-menu"));
        forceOnePanelAlwaysVisible();
        super.getItems().addAll(statusBarVisibility, toolBarVisibility, new SeparatorMenuItem(),
                leftPanelVisibility, rightPanelVisibility, new SeparatorMenuItem(),
                hiddenFilesVisibility);
    }

    public static BooleanProperty hiddenFilesVisible() {
        return hiddenFilesVisibility.selectedProperty();
    }

    private static CheckMenuItem createHiddenFilesVisibilityCheck() {
        CheckMenuItem hiddenFiles = new CheckMenuItem(getString("hidden-files"));
        hiddenFiles.setSelected(Boolean.valueOf(getSetting(Setting.HIDDEN_FILES_VISIBLE)));
        hiddenFiles.selectedProperty().addListener((observable, oldValue, newValue) -> {
            DirectoryView.showHiddenFiles = newValue;
            ConfigManager.setProperty(Setting.HIDDEN_FILES_VISIBLE, newValue);
            MainWindow.getLeftDirectoryView().reload();
            MainWindow.getRightDirectoryView().reload();
        });
        return hiddenFiles;
    }

    private static CheckMenuItem createRightPanelVisibilityCheck() {
        CheckMenuItem rightPanelVisibility = new CheckMenuItem(getString("right-panel"));
        rightPanelVisibility.setSelected(Boolean.valueOf(getSetting(Setting.RIGHT_VIEW_VISIBLE)));
        rightPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ConfigManager.setProperty(Setting.RIGHT_VIEW_VISIBLE, newValue);
            MainWindow.getRightWrapper().managedProperty().setValue(newValue);
        });
        return rightPanelVisibility;
    }

    private static CheckMenuItem createLeftPanelVisibilityCheck() {
        CheckMenuItem leftPanelVisibility = new CheckMenuItem(getString("left-panel"));
        leftPanelVisibility.setSelected(Boolean.valueOf(getSetting(Setting.LEFT_VIEW_VISIBLE)));
        leftPanelVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ConfigManager.setProperty(Setting.LEFT_VIEW_VISIBLE, newValue);
            MainWindow.getLeftWrapper().managedProperty().setValue(newValue);
        });
        return leftPanelVisibility;
    }

    private static CheckMenuItem createStatusBarVisibilityCheck() {
        CheckMenuItem statusBarVisibility = new CheckMenuItem(getString("status-bar-visibility-check"));
        statusBarVisibility.setSelected(Boolean.valueOf(getSetting(Setting.STATUS_BAR_VISIBLE)));
        statusBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ConfigManager.setProperty(Setting.STATUS_BAR_VISIBLE, newValue);
            MainWindow.getStatusBar().managedProperty().setValue(newValue);
        });
        return statusBarVisibility;
    }

    private static CheckMenuItem createToolBarVisibilityCheck() {
        CheckMenuItem toolBarVisibility = new CheckMenuItem(getString("tool-bar"));
        toolBarVisibility.setSelected(Boolean.valueOf(getSetting(Setting.TOOL_BAR_VISIBLE)));
        toolBarVisibility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ConfigManager.setProperty(Setting.TOOL_BAR_VISIBLE, newValue);
            MainWindow.getMainWindowToolBar().managedProperty().setValue(newValue);
        });
        return toolBarVisibility;
    }

    private void forceOnePanelAlwaysVisible() {
        leftPanelVisibility.disableProperty().bind(rightPanelVisibility.selectedProperty().not());
        rightPanelVisibility.disableProperty().bind(leftPanelVisibility.selectedProperty().not());
    }
}
