package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import pl.greywarden.openr.i18n.I18nManager;

public class ConfirmExitDialog extends Alert {

    public ConfirmExitDialog() {
        super(AlertType.CONFIRMATION);
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("confirmation-dialog");
        ButtonType yes = new ButtonType(i18n.getString("yes"), ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(i18n.getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
        super.getButtonTypes().setAll(yes, no);
        super.setTitle(i18n.getString("confirm-exit"));
        super.setHeaderText(i18n.getString("confirm-exit-header"));
    }

}
