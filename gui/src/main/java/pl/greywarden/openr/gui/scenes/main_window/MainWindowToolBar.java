package pl.greywarden.openr.gui.scenes.main_window;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.stage.WindowEvent;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.find.FindWindow;
import pl.greywarden.openr.gui.grep.GrepWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class MainWindowToolBar extends ToolBar {

    public MainWindowToolBar() {
        super();
        createToolBar();
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
        exit.setGraphic(IconManager.getIcon("exit"));
        exit.tooltipProperty().setValue(new Tooltip(getString("exit-menu-item")));
        exit.setOnAction(event -> fireClosingEvent());
        return exit;
    }

    private void fireClosingEvent() {
        super.fireEvent(new WindowEvent(this.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private Button createFindButton() {
        Button find = new Button();
        find.setGraphic(IconManager.getIcon("find"));
        find.tooltipProperty().setValue(new Tooltip(getString("find-file")));
        find.setOnAction(event -> new FindWindow());
        return find;
    }

    private Button createGrepButton() {
        Button grep = new Button();
        grep.setGraphic(IconManager.getIcon("grep"));
        grep.tooltipProperty().setValue(new Tooltip(getString("grep-window-title")));
        grep.setOnAction(event -> new GrepWindow());
        return grep;
    }

    private Button createNewFileButton() {
        Button newFile = new Button();
        newFile.setGraphic(IconManager.getIcon("new-file"));
        newFile.tooltipProperty().setValue(new Tooltip(getString("create-file")));
        newFile.setOnAction(event -> new CreateFileDialog().showDialog());
        return newFile;
    }
}
