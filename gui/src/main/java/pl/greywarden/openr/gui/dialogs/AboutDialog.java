package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import pl.greywarden.openr.i18n.I18nManager;

import java.io.IOException;
import java.util.Properties;

public class AboutDialog extends Alert {

    public AboutDialog() {
        super(AlertType.INFORMATION);
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("about");
        super.setTitle(i18n.getString("title"));
        super.setHeaderText("OpenR");
        super.setContentText(i18n.getString("content").replace("$ver", getVersion()));
    }

    public static String getVersion() {
        Properties version = new Properties();
        try {
            version.load(AboutDialog.class.getClassLoader().getResourceAsStream("version.properties"));
            return version.getProperty("version");
        } catch (IOException e) {
            return "";
        }
    }

}
