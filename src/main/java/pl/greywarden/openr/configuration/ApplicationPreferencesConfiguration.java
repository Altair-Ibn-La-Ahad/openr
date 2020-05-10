package pl.greywarden.openr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.prefs.Preferences;

@Configuration
public class ApplicationPreferencesConfiguration {
    private static final String OPENR_CONFIGURATION_NODE = "pl.greywarden.openr.OpenR";

    @Bean
    Preferences applicationSettings() {
        return Preferences.userRoot().node(OPENR_CONFIGURATION_NODE);
    }
}
