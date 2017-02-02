package pl.greywarden.openr.gui.scenes.main_window;

import javafx.scene.control.MenuBar;
import pl.greywarden.openr.gui.scenes.main_window.menu.File;
import pl.greywarden.openr.gui.scenes.main_window.menu.Help;
import pl.greywarden.openr.gui.scenes.main_window.menu.View;

public class MainWindowMenuBar extends MenuBar {

    public MainWindowMenuBar() {
        super();
        super.getMenus().addAll(new File(), new View(), new Help());
    }
}
