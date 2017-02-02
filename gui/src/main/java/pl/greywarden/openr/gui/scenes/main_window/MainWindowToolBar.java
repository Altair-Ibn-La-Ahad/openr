package pl.greywarden.openr.gui.scenes.main_window;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.stage.WindowEvent;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.find.FindWindow;
import pl.greywarden.openr.gui.grep.GrepWindow;

public class MainWindowToolBar extends ToolBar {

    public MainWindowToolBar() {
        super();

        Button newFile = new Button();
        newFile.setGraphic(IconManager.getIcon("new-file"));
        newFile.setOnAction(event -> new CreateFileDialog().showDialog());

        Button grep = new Button();
        grep.setGraphic(IconManager.getIcon("grep"));
        grep.setOnAction(event -> new GrepWindow());

        Button find = new Button();
        find.setGraphic(IconManager.getIcon("find"));
        find.setOnAction(event -> new FindWindow());

        Button exit = new Button();
        exit.setGraphic(IconManager.getIcon("exit"));
        exit.setOnAction(event -> super.fireEvent(
                new WindowEvent(this.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));

        super.getItems().addAll(newFile, new Separator(), grep, find, new Separator(), exit);
    }
}
