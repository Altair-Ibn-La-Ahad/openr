package pl.greywarden.openr.component;

import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

public class LabelWithIcon extends Label {
    public LabelWithIcon(String text, String iconCode) {
        super(text);
        super.setGraphic(new FontIcon(iconCode));
    }
}
