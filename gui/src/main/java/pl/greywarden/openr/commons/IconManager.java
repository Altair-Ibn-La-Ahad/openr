package pl.greywarden.openr.commons;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.SimpleBufferedImageFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.greywarden.openr.templates.Template;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IconManager {

    private static final Map<String, String> icons = loadIconBindingsFromFile();

    private static final String filesystemIconsPath = "icons/filesystem/";
    private static final String programIconsPath = "icons/program/";
    private static final int smallIcon = 16;
    private static final int bigIcon = 32;

    private static Map<String, String> loadIconBindingsFromFile() {
        Map<String, String> result = new HashMap<>();
        String jsonString = getIconsJson();
        JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("icons");
        jsonArray.forEach(o -> {
            JSONObject icon = (JSONObject) o;
            String iconName = icon.getString("icon");
            icon.getJSONArray("extensions").forEach(ext -> result.put(ext.toString(), iconName));
        });
        return result;
    }

    private static String getIconsJson() {
        try {
            return IOUtils.toString(Template.class.getClassLoader()
                    .getResourceAsStream("file_icons.json"), "UTF-8");
        } catch (IOException e) {
            return "{}";
        }
    }

    public static ImageView getProgramIcon(String name) {
        InputStream resource = IconManager.class.getClassLoader()
                .getResourceAsStream(programIconsPath + name + ".png");
        return resource == null ? new ImageView() :
                new ImageView(new Image(resource, smallIcon, smallIcon, true, true));
    }

    public static Image getFileIconSmall(String path) {
        return getFileIcon(path, smallIcon);
    }

    public static Image getFileIconBig(String path) {
        return getFileIcon(path, bigIcon);
    }

    private static Image getFileIcon(String path, int sizeXY) {
        File file = new File(path);
        InputStream iconInputStream;
        if (file.isDirectory()) {
            iconInputStream = getIconByName("directory");
        } else {
            iconInputStream = getIconByName(FilenameUtils.getExtension(file.getName()).toLowerCase());
        }
        return new Image(iconInputStream, sizeXY, sizeXY, true, true);
    }

    private static InputStream getIconByName(String name) {
        String iconName = "directory".equals(name) ? "directory" : icons.getOrDefault(name, "default");
        return IconManager.class.getClassLoader()
                .getResourceAsStream(filesystemIconsPath + iconName + ".png");
    }

    private static ImageView getIconFromPath(String path, int sizeXY) {
        try {
            final Map<String, Object> renderingParams = new HashMap<>();
            renderingParams.put(ImagingConstants.BUFFERED_IMAGE_FACTORY, new SimpleBufferedImageFactory());
            WritableImage resultImage = new WritableImage(sizeXY, sizeXY);
            BufferedImage image = Imaging.getBufferedImage(new File(path), renderingParams);
            ImageView result = new ImageView(SwingFXUtils.toFXImage(image, resultImage));
            result.setFitHeight(sizeXY);
            result.setFitWidth(sizeXY);
            result.setSmooth(true);
            return result;
        } catch (IOException | ImageReadException e) {
            return new ImageView();
        }
    }

    public static ImageView getSmallIconFromPath(String path) {
        return getIconFromPath(path, smallIcon);
    }

    public static ImageView getBigIconFromPath(String path) {
        return getIconFromPath(path, bigIcon);
    }
}
