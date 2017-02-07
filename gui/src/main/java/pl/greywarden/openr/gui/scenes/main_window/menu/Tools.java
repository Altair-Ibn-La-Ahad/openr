package pl.greywarden.openr.gui.scenes.main_window.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.find.FindWindow;
import pl.greywarden.openr.gui.grep.GrepWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class Tools extends Menu {

    public Tools() {
        super(getString("tools"));
        createMenu();
    }

    private void createMenu() {
        MenuItem grep = createGrepMenuItem();
        MenuItem find = createFindMenuItem();

        super.getItems().addAll(grep, find);
    }

    private MenuItem createFindMenuItem() {
        MenuItem find = new MenuItem(getString("find-file"));
        find.setGraphic(IconManager.getIcon("find"));
        find.setOnAction(event -> new FindWindow());
        return find;
    }

    private MenuItem createGrepMenuItem() {
        MenuItem grep = new MenuItem(getString("grep-window-title"));
        grep.setGraphic(IconManager.getIcon("grep"));
        grep.setOnAction(event -> new GrepWindow());
        return grep;
    }
}
