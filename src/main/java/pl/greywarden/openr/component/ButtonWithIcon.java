package pl.greywarden.openr.component;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;
import org.kordamp.ikonli.javafx.FontIcon;

public class ButtonWithIcon extends Button {
    public ButtonWithIcon(@NamedArg("icon") String iconLiteral) {
        this(iconLiteral, 24);
    }

    public ButtonWithIcon(String iconLiteral, int iconSize) {
        super();
        var icon = new FontIcon(iconLiteral);
        setGraphic(icon);
        icon.setIconSize(iconSize);
        icon.setIconLiteral(iconLiteral);
    }
}
