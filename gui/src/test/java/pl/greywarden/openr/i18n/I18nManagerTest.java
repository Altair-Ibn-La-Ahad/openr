package pl.greywarden.openr.i18n;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class I18nManagerTest {

    private final I18nManager i18n = I18nManager.getInstance();

    @Before
    public void setUp() {
        i18n.setBundle("test-bundle");
    }

    @Test
    public void getString() {
        Locale.setDefault(Locale.forLanguageTag("pl"));
        System.err.printf("Locale: %s\nBundle: %s\nText: %s\n\n",
                Locale.getDefault(),
                i18n.getBundle(),
                i18n.getString("test"));

        Locale.setDefault(Locale.forLanguageTag("en"));
        System.err.printf("Locale: %s\nBundle: %s\nText: %s\n\n",
                Locale.getDefault(),
                i18n.getBundle(),
                i18n.getString("test"));

        Locale.setDefault(Locale.forLanguageTag("zh"));
        System.err.printf("Locale: %s\nBundle: %s\nText: %s\n\n",
                Locale.getDefault(),
                i18n.getBundle(),
                i18n.getString("test"));
    }

    @Test
    public void getSupportedLocales() {
        i18n.getSupportedLocales().forEach((k, v) ->
                System.err.printf("Language tag: %s\nLocale: %s\n\n", k, v));
    }

}