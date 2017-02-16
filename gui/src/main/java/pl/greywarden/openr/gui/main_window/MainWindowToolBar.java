package pl.greywarden.openr.gui.main_window;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.stage.WindowEvent;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.dialogs.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.dialogs.find.FindWindow;
import pl.greywarden.openr.gui.dialogs.grep.GrepWindow;

import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class MainWindowToolBar extends ToolBar {

    public MainWindowToolBar() {
        super();
        super.managedProperty().setValue(Boolean.valueOf(getSetting(Setting.TOOL_BAR_VISIBLE)));
        super.managedProperty().addListener((observable, oldValue, newValue) ->
                ConfigManager.setProperty(Setting.TOOL_BAR_VISIBLE, newValue));
        createToolBar();
        super.visibleProperty().bind(managedProperty());
    }

    private void createToolBar() {
        Button newFile = createNewFileButton();
        Button grep = createGrepButton();
        Button find = createFindButton();
        Button exit = createExitButton();

        super.getItems().addAll(newFile, new Separator(), grep, find, new Separator(), exit);
    }

    private Button createExitButton() {
        Button exit = new Button();
        exit.setGraphic(IconManager.getProgramIcon("exit"));
        exit.tooltipProperty().setValue(new Tooltip(getString("exit-menu-item")));
        exit.setOnAction(event -> fireClosingEvent());
        return exit;
    }

    private void fireClosingEvent() {
        super.fireEvent(new WindowEvent(this.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private Button createFindButton() {
        Button find = new Button();
        find.setGraphic(IconManager.getProgramIcon("find"));
        find.tooltipProperty().setValue(new Tooltip(getString("find-file")));
        find.setOnAction(event -> new FindWindow());
        return find;
    }

    private Button createGrepButton() {
        Button grep = new Button();
        grep.setGraphic(IconManager.getProgramIcon("grep"));
        grep.tooltipProperty().setValue(new Tooltip(getString("grep-window-title")));
        grep.setOnAction(event -> new GrepWindow());
        return grep;
    }

    private Button createNewFileButton() {
        Button newFile = new Button();
        newFile.setGraphic(IconManager.getProgramIcon("new-file"));
        newFile.tooltipProperty().setValue(new Tooltip(getString("create-file")));
        newFile.setOnAction(event -> new CreateFileDialog());
        return newFile;
    }
}