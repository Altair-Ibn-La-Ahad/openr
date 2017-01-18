package pl.greywarden.openr.i18n;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log4j
public class I18nManager {

    private static I18nManager instance;
    private final Map<String, Locale> supportedLocales;
    private String bundle;
    private Locale actualLocale;
    {
        supportedLocales = new HashMap<>();
        supportedLocales.put("en", Locale.forLanguageTag("en"));
        supportedLocales.put("pl", Locale.forLanguageTag("pl"));

        bundle = "default";
    }
    private I18nManager() {

    }
    public static I18nManager getInstance() {
        return instance == null ? instance = new I18nManager() : instance;
    }

    public void setBundle(@NonNull String bundle) {
        this.bundle = bundle;
    }

    public String getBundle() {
        return bundle;
    }

    public String getString(@NonNull String key) {
        try {
            return ResourceBundle.getBundle("i18n/" + bundle, getActualLocale(),
                    I18nManager.class.getClassLoader()).getString(key);
        } catch (MissingResourceException exception) {
            log.error("Unable to find key for specified bundle", exception);
            return bundle + "." + key;
        }
    }

    public Map<String, Locale> getSupportedLocales() {
        return this.supportedLocales;
    }

    public void setActualLocale(String languageCode) {
        actualLocale = supportedLocales.getOrDefault(languageCode, Locale.forLanguageTag("en"));
    }

    private Locale getActualLocale() {
        return supportedLocales.containsKey(Locale.getDefault().getLanguage())
                ? Locale.getDefault() : supportedLocales.get("en");
    }
}
