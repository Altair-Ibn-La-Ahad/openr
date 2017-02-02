package pl.greywarden.openr.gui.dialogs;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import pl.greywarden.openr.commons.I18nManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import java.util.Locale;
import java.util.Optional;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class Settings extends Dialog<ButtonType> {

    private ButtonType ok = CommonButtons.OK;

    private ComboBox<Locale> selectLocale;

    private CheckBox keepClipboard;
    private CheckBox confirmClose;

    private boolean reloadRequired;

    public Settings() {
        super();
        super.setTitle(getString("settings"));

        ButtonType cancel = CommonButtons.CANCEL;
        ButtonType apply = CommonButtons.APPLY;
        super.getDialogPane().getButtonTypes().setAll(ok, apply, cancel);
        Node buttonApply = super.getDialogPane().lookupButton(apply);
        buttonApply.setDisable(true);
        buttonApply.addEventFilter(ActionEvent.ACTION, event -> {
            saveSettings();
            buttonApply.setDisable(true);
            event.consume();
        });

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);

        Label languageLabel = new Label(getString("language") + ":");
        selectLocale = new ComboBox<>();
        selectLocale.setButtonCell(selectLocaleButtonCell());
        selectLocale.setCellFactory(param -> selectLocaleButtonCell());
        selectLocale.getItems().setAll(I18nManager.getSupportedLocales().values());
        selectLocale.getSelectionModel().select(I18nManager.getActualLocale());
        selectLocale.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    reloadRequired = true;
                    buttonApply.setDisable(false);
                });

        Label keepClipboardLabel = new Label(getString("keep-clipboard") + "?");
        keepClipboard = new CheckBox();
        keepClipboard.setSelected(Boolean.valueOf(ConfigManager.getSetting(Setting.KEEP_CLIPBOARD.CODE)));
        keepClipboard.selectedProperty().addListener((observable, oldValue, newValue) -> buttonApply.setDisable(false));

        Label confirmCloseLabel = new Label(getString("confirm-close") + "?");
        confirmClose = new CheckBox();
        confirmClose.setSelected(Boolean.valueOf(ConfigManager.getSetting(Setting.CONFIRM_CLOSE.CODE)));
        confirmClose.selectedProperty().addListener((observable, oldValue, newValue) -> buttonApply.setDisable(false));

        GridPane.setHalignment(keepClipboard, HPos.CENTER);
        GridPane.setHalignment(confirmClose, HPos.CENTER);
        layout.addRow(0, languageLabel, selectLocale);
        layout.addRow(1, keepClipboardLabel, keepClipboard);
        layout.addRow(2, confirmCloseLabel, confirmClose);

        super.getDialogPane().setContent(layout);
    }

    private ListCell<Locale> selectLocaleButtonCell() {
        return new ListCell<Locale>() {
            @Override
            protected void updateItem(Locale loc, boolean empty) {
                super.updateItem(loc, empty);
                if (!empty) {
                    setText(loc.getDisplayLanguage(I18nManager.getActualLocale()));
                }
            }
        };
    }

    public void showDialog() {
        Optional<ButtonType> result = showAndWait();
        result.ifPresent(buttonType -> {
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
                Setting.LANGUAGE.CODE,
                selectLocale.getSelectionModel().getSelectedItem().getLanguage());

        ConfigManager.setProperty(
                Setting.KEEP_CLIPBOARD.CODE,
                Boolean.toString(keepClipboard.isSelected()));

        ConfigManager.setProperty(
                Setting.CONFIRM_CLOSE.CODE,
                Boolean.toString(confirmClose.isSelected()));

    }
}
