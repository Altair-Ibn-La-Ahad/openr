package pl.greywarden.openr.configuration;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.SystemUtils;
import pl.greywarden.openr.i18n.I18nManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

@Log4j
public class ConfigManager {

    private static Properties savedProperties;
    private static final Properties defaultProperties;

    static {
        savedProperties = new Properties();
        defaultProperties = new Properties();
        createDefaults();
        try {
            File configFile = getConfigFile();
            savedProperties.load(new FileInputStream(configFile));
        } catch (IOException | URISyntaxException e) {
            log.warn("Exception during loading saved prefs, using defaults");
            savedProperties = defaultProperties;
        }
    }

    private ConfigManager() {

    }

    private static void createDefaults() {
        defaultProperties.put(Settings.LEFT_DIR.CODE, SystemUtils.getUserDir().getAbsolutePath());
        defaultProperties.put(Settings.RIGHT_DIR.CODE, SystemUtils.getUserDir().getAbsolutePath());
        defaultProperties.put(Settings.LANGUAGE.CODE, I18nManager.getActualLocale().getLanguage());
    }

    public static String getSetting(String key) {
        return savedProperties.get(key).toString();
    }

    private static File getConfigFile() throws URISyntaxException {
        URL configFileUrl = ConfigManager.class.getProtectionDomain().getCodeSource().getLocation();
        File parentDir = new File(configFileUrl.toURI()).getParentFile();
        return new File(parentDir, "settings.properties");
    }

    public static void storeSettings() {
        try {
            OutputStream out = new FileOutputStream(getConfigFile());
            savedProperties.store(out, "");
        } catch (IOException | URISyntaxException e) {
            log.error("Exception during storing user preferences", e);
        }
    }

    public static void setProperty(String key, String value) {
        savedProperties.setProperty(key, value);
    }

}
