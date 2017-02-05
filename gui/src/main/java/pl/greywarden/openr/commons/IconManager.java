package pl.greywarden.openr.commons;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IconManager {

    public static ImageView getIcon(String name) {
        InputStream resource = IconManager.class.getClassLoader()
                .getResourceAsStream("icons/" + name + ".png");
        return resource != null ? new ImageView(new Image(resource)) : null;
    }

    public static Image getFileIcon(String path) {
        File file = new File(path);
        java.awt.Image awtImage = ((ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file)).getImage();
        BufferedImage bImg;
        if (awtImage instanceof BufferedImage) {
            bImg = (BufferedImage) awtImage;
        } else {
            bImg = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bImg.createGraphics();
            graphics.drawImage(awtImage, 0, 0, null);
            graphics.dispose();
        }
        return SwingFXUtils.toFXImage(bImg, null);
    }
}
