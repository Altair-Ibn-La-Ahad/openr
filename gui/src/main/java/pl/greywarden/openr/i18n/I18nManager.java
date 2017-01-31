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

    private static final Map<String, Locale> supportedLocales;
    private static Locale actualLocale;
    static {
        supportedLocales = new HashMap<>();
        supportedLocales.put("en", Locale.forLanguageTag("en"));
        supportedLocales.put("pl", Locale.forLanguageTag("pl"));

        actualLocale = Locale.getDefault();
    }
    private I18nManager() {

    }

    public static String getString(@NonNull String key) {
        try {
            return ResourceBundle.getBundle("i18n/strings", getActualLocale(),
                    I18nManager.class.getClassLoader()).getString(key);
        } catch (MissingResourceException exception) {
            log.error("Unable to find key for specified bundle", exception);
            return key;
        }
    }

    public static Map<String, Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public static void setLocale(String languageCode) {
        actualLocale = supportedLocales.getOrDefault(languageCode, Locale.forLanguageTag("en"));
    }

    public static Locale getActualLocale() {
        return supportedLocales.containsKey(actualLocale.getLanguage())
                ? actualLocale : Locale.forLanguageTag("en");
    }
}
