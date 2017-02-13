package pl.greywarden.openr.gui.settings;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import pl.greywarden.openr.commons.I18nManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class Settings extends Dialog<ButtonType> {

    private LocaleComboBox selectLocale;

    private CheckBox keepClipboard;
    private CheckBox confirmClose;

    private static boolean reloadRequired;
    private final GridPane layout = new GridPane();
    private Label languageLabel;
    private Label keepClipboardLabel;
    private Label confirmCloseLabel = new Label(getString("confirm-close") + "?");

    private final ButtonType apply = CommonButtons.APPLY;
    private final ButtonType ok = CommonButtons.OK;

    public Settings() {
        super();
        super.setTitle(getString("settings"));

        createLayout();
        createSelectLangLabelAndCombo();
        createKeepClipboardLabelAndCheck();
        createConfirmCloseLabelAndCheck();
        centerCheckBoxes();
        createSettingsDialogLayout();

        super.getDialogPane().setContent(layout);
        createButtons();
        showDialog();
    }

    private void createButtons() {
        ButtonType cancel = CommonButtons.CANCEL;
        super.getDialogPane().getButtonTypes().setAll(ok, cancel, apply);
        getApplyButton().setDisable(true);
        getApplyButton().addEventFilter(ActionEvent.ACTION, this::handleApplyEvent);
    }

    private void createSettingsDialogLayout() {
        layout.addRow(0, languageLabel, selectLocale);
        layout.addRow(1, keepClipboardLabel, keepClipboard);
        layout.addRow(2, confirmCloseLabel, confirmClose);
    }

    private void centerCheckBoxes() {
        GridPane.setHalignment(keepClipboard, HPos.CENTER);
        GridPane.setHalignment(confirmClose, HPos.CENTER);
    }

    private void createConfirmCloseLabelAndCheck() {
        confirmCloseLabel = new Label(getString("confirm-close") + "?");
        confirmClose = new CheckBox();
        confirmClose.setSelected(Boolean.valueOf(getSetting(Setting.CONFIRM_CLOSE)));
        confirmClose.selectedProperty().addListener((observable, oldValue, newValue) -> enableApplyButton());
    }

    private void createKeepClipboardLabelAndCheck() {
        keepClipboardLabel = new Label(getString("keep-clipboard") + "?");
        keepClipboard = new CheckBox();
        keepClipboard.setSelected(Boolean.valueOf(getSetting(Setting.KEEP_CLIPBOARD)));
        keepClipboard.selectedProperty().addListener((observable, oldValue, newValue) -> enableApplyButton());
    }

    private void createSelectLangLabelAndCombo() {
        languageLabel = new Label(getString("language") + ":");
        selectLocale = new LocaleComboBox();
        selectLocale.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    reloadRequired = true;
                    enableApplyButton();
                });
    }

    private void createLayout() {
        layout.setHgap(10);
        layout.setVgap(10);
    }

    private void enableApplyButton() {
        getApplyButton().setDisable(false);
    }

    private void disableApplyButton() {
        getApplyButton().setDisable(true);
    }

    private Node getApplyButton() {
        return super.getDialogPane().lookupButton(apply);
    }

    private void handleApplyEvent(ActionEvent event) {
        saveSettings();
        disableApplyButton();
        event.consume();
    }

    private void showDialog() {
        showAndWait().ifPresent(buttonType -> {
            if (ok.equals(buttonType)) {
                saveSettings();
                super.close();
                if (reloadRequired) {
                    MainWindow.getInstance().reload();
                }
            }
        });
    }

    private void saveSettings() {
        I18nManager.setLocale(selectLocale.getSelectionModel().getSelectedItem().getLanguage());
        ConfigManager.setProperty(
                Setting.LANGUAGE,
                selectLocale.getSelectionModel().getSelectedItem().getLanguage());

        ConfigManager.setProperty(
                Setting.KEEP_CLIPBOARD,
                keepClipboard.isSelected());

        ConfigManager.setProperty(
                Setting.CONFIRM_CLOSE,
                confirmClose.isSelected());

    }
}
