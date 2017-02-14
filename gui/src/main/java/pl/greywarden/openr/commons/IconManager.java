package pl.greywarden.openr.commons;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IconManager {

    private static final Map<String, String> icons = loadIconBindingsFromFile();

    private static final String filesystemIconsPath = "icons/filesystem/";
    private static final String programIconsPath = "icons/program/";

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
        return resource != null ? new ImageView(new Image(resource)) : null;
    }

    public static Image getFileIconSmall(String path) {
        return getFileIcon(path, 16);
    }

    public static Image getFileIconBig(String path) {
        return getFileIcon(path, 32);
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

    public static ImageView getIconFromPath(String path) {
        try {
            return new ImageView(
                    new Image(new FileInputStream(
                            new File(path)), 16, 16, true, true));
        } catch (FileNotFoundException e) {
            return new ImageView();
        }

    }
}
