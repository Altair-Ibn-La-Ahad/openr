package pl.greywarden.openr.gui.main_window;

import javafx.scene.control.MenuBar;
import pl.greywarden.openr.gui.menu.file.File;
import pl.greywarden.openr.gui.menu.Help;
import pl.greywarden.openr.gui.menu.Tools;
import pl.greywarden.openr.gui.menu.View;
import pl.greywarden.openr.gui.menu.favourite_programs.FavouriteProgramsMenu;

public class MainWindowMenuBar extends MenuBar {

    public MainWindowMenuBar() {
        super();
        super.getMenus().addAll(new File(), View.getInstance(), new Tools(),
                new FavouriteProgramsMenu(), new Help());
    }
}
