package pl.greywarden.openr.gui.dialogs.settings;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import pl.greywarden.openr.commons.I18nManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.dialogs.putty.SetPuttyPathDialog;
import pl.greywarden.openr.gui.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class SettingsDialog extends Dialog<ButtonType> {

    private LocaleComboBox selectLocale;

    private CheckBox keepClipboard;
    private CheckBox confirmClose;
    private Button setPuttyPath;

    private static boolean reloadRequired;
    private final GridPane layout = new GridPane();
    private Label languageLabel;
    private Label keepClipboardLabel;
    private Label confirmCloseLabel = new Label(getString("confirm-close") + "?");
    private final Label puttyPathLabel = new Label("PuTTY");

    private final ButtonType apply = CommonButtons.APPLY;
    private final ButtonType ok = CommonButtons.OK;

    public SettingsDialog() {
        super();
        super.setTitle(getString("settings"));

        createLayout();
        createSelectLangLabelAndCombo();
        createKeepClipboardLabelAndCheck();
        createConfirmCloseLabelAndCheck();
        centerCheckBoxes();
        createPuttyPathCustomization();
        createSettingsDialogLayout();

        super.getDialogPane().setContent(layout);
        createButtons();
        showDialog();
    }

    private void createPuttyPathCustomization() {
        setPuttyPath = new Button("...");
        setPuttyPath.setOnAction(event -> new SetPuttyPathDialog(ConfigManager.getSetting(Setting.PUTTY)));
        GridPane.setHalignment(setPuttyPath, HPos.CENTER);
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
        layout.addRow(3, puttyPathLabel, setPuttyPath);
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
        HBox.setHgrow(selectLocale, Priority.ALWAYS);
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
        I18nManager.setLocale(selectLocale.getSelectedLocale().getLanguage());
        ConfigManager.setProperty(
                Setting.LANGUAGE,
                selectLocale.getSelectedLocale().getLanguage());

        ConfigManager.setProperty(
                Setting.KEEP_CLIPBOARD,
                keepClipboard.isSelected());

        ConfigManager.setProperty(
                Setting.CONFIRM_CLOSE,
                confirmClose.isSelected());

    }
}
