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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IconManager {

    private static String getProgramIconResourceName(String iconName) {
        return String.format("icons%sprogram%s%s.png", File.separator, File.separator, iconName);
    }

    public static ImageView getProgramIcon(String name) {
        InputStream resource = IconManager.class.getClassLoader()
                .getResourceAsStream(getProgramIconResourceName(name));
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

    public static ImageView getIconFromPath(String path) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            return new ImageView();
        }
        ImageView result = new ImageView(new Image(inputStream));
        result.setFitHeight(16.0);
        result.setFitWidth(16.0);
        return result;
    }
}
