package pl.greywarden.openr.gui.menu;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.help.HelpWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class Help extends Menu {

    public Help() {
        super(getString("help-menu"));

        MenuItem about = new MenuItem(getString("about-menu-item"));
        about.setOnAction(event -> new AboutDialog().show());

        MenuItem help = new MenuItem(getString("help-menu-item"));
        help.setGraphic(IconManager.getIcon("help"));
        help.setOnAction(event -> Platform.runLater(HelpWindow::new));

        super.getItems().addAll(about, help);
    }
}
