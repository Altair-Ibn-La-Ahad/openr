package pl.greywarden.openr.gui.scenes.main_window;

import javafx.scene.control.MenuBar;
import pl.greywarden.openr.gui.scenes.main_window.menu.File;
import pl.greywarden.openr.gui.scenes.main_window.menu.Help;
import pl.greywarden.openr.gui.scenes.main_window.menu.Tools;
import pl.greywarden.openr.gui.scenes.main_window.menu.View;
import pl.greywarden.openr.gui.scenes.main_window.menu.favourite_programs.FavouriteProgramsMenu;

public class MainWindowMenuBar extends MenuBar {

    public MainWindowMenuBar() {
        super();
        super.getMenus().addAll(new File(), new View(), new Tools(),
                new FavouriteProgramsMenu(), new Help());
    }
}
