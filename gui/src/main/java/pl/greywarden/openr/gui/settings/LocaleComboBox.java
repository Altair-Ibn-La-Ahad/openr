package pl.greywarden.openr.gui.settings;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import pl.greywarden.openr.commons.I18nManager;

import java.util.Locale;

public class LocaleComboBox extends ComboBox<Locale> {

    public LocaleComboBox() {
        super();
        super.getItems().setAll(I18nManager.getSupportedLocales().values());
        super.getSelectionModel().select(I18nManager.getActualLocale());

        super.setButtonCell(localeButtonCell());
        super.setCellFactory(param -> localeButtonCell());
    }

    private ListCell<Locale> localeButtonCell() {
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
}
