package pl.greywarden.openr.gui.dialogs.help;

import pl.greywarden.openr.commons.I18nManager;

import java.net.URL;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class HelpTopicWrapper {

    private final String topic;

    public HelpTopicWrapper(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return getString(topic + "-help-topic");
    }

    public String getUrlToContent() {
        URL url = this.getClass().getClassLoader()
                .getResource("help/" + topic + "_" +  I18nManager.getActualLocale().getLanguage() + ".html");
        return url != null ? url.toString() : "";
    }

}
