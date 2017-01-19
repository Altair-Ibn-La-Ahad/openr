package pl.greywarden.openr.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class IconManager {

    public static ImageView getIcon(String name) {
        InputStream resource = IconManager.class.getClassLoader()
                .getResourceAsStream("icons/" + name + ".png");
        return resource != null ? new ImageView(new Image(resource)) : null;
    }

}
