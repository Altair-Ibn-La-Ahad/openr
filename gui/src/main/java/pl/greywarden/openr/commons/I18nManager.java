package pl.greywarden.openr.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.gui.dialogs.CommonButtons;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nManager {

    private static final Map<String, Locale> supportedLocales;
    private static Locale actualLocale;
    static {
        supportedLocales = new HashMap<>();
        supportedLocales.put("en", Locale.forLanguageTag("en"));
        supportedLocales.put("pl", Locale.forLanguageTag("pl"));

        actualLocale = Locale.getDefault();
    }

    public static String getString(@NonNull String key) {
        try {
            return ResourceBundle.getBundle("i18n/strings", getActualLocale(),
                    I18nManager.class.getClassLoader()).getString(key);
        } catch (MissingResourceException exception) {
            log.warn(String.format("No value for key %s", key));
            return key;
        }
    }

    public static Map<String, Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public static void setLocale(String languageCode) {
        actualLocale = supportedLocales.getOrDefault(languageCode, Locale.forLanguageTag("en"));
        CommonButtons.reinitialize();
    }

    public static Locale getActualLocale() {
        return supportedLocales.containsKey(actualLocale.getLanguage())
                ? actualLocale : Locale.forLanguageTag("en");
    }
}
