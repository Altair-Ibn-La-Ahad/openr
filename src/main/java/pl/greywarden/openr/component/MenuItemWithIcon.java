package pl.greywarden.openr.component;

import javafx.beans.NamedArg;
import javafx.scene.control.MenuItem;
import org.kordamp.ikonli.javafx.FontIcon;

public class MenuItemWithIcon extends MenuItem {
    public MenuItemWithIcon(@NamedArg("icon") String iconLiteral) {
        super();
        var icon = new FontIcon(iconLiteral);
        super.setGraphic(icon);
    }
}
