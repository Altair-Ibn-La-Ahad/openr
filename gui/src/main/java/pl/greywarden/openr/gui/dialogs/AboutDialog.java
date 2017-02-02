package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import java.io.IOException;
import java.util.Properties;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class AboutDialog extends Alert {

    public AboutDialog() {
        super(AlertType.INFORMATION);
        super.setTitle(getString("about-title"));
        super.setHeaderText("OpenR");
        super.setContentText(getString("about-content").replace("$ver", getVersion()));
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
