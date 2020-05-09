package pl.greywarden.openr.service;

import org.springframework.stereotype.Service;

import java.util.prefs.Preferences;

@Service
public class ApplicationSettingsService {
    private static final String OPENR_CONFIGURATION_NODE = "pl.greywarden.openr.OpenR";
    private final Preferences preferences = Preferences.userRoot().node(OPENR_CONFIGURATION_NODE);
}
