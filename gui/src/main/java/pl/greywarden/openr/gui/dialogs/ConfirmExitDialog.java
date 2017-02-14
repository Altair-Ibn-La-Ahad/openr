package pl.greywarden.openr.gui.dialogs;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.main_window.MainWindow;

import java.util.function.Predicate;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class ConfirmExitDialog extends Alert {

    public ConfirmExitDialog(WindowEvent event) {
        super(AlertType.CONFIRMATION);
        super.getButtonTypes().setAll(CommonButtons.YES, CommonButtons.NO);
        super.setTitle(getString("confirm-exit-title"));
        super.setHeaderText(getString("confirm-exit-header"));
        if (askForConfirm()) {
            showConfirmDialog();
        } else {
            storeSettingsAndExit();
        }
        event.consume();
    }

    private void showConfirmDialog() {
        super.showAndWait().filter(selectedYesOption()).ifPresent(b -> storeSettingsAndExit());
    }

    private Boolean askForConfirm() {
        return Boolean.valueOf(getSetting(Setting.CONFIRM_CLOSE));
    }

    private Predicate<ButtonType> selectedYesOption() {
        return buttonType -> buttonType.equals(CommonButtons.YES);
    }

    private void storeSettingsAndExit() {
        ConfigManager.setProperty(Setting.LEFT_DIR,
                MainWindow.getLeftDirectoryView().getRootPath());
        ConfigManager.setProperty(Setting.RIGHT_DIR,
                MainWindow.getRightDirectoryView().getRootPath());
        ConfigManager.storeSettings();

        FavouritePrograms.storeProgramsToFile();

        Platform.exit();
    }

}
