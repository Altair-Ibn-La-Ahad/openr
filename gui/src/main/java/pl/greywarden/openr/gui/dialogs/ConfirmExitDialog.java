package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import static pl.greywarden.openr.i18n.I18nManager.getString;

public class ConfirmExitDialog extends Alert {

    public ConfirmExitDialog() {
        super(AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType(getString("yes"), ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
        super.getButtonTypes().setAll(yes, no);
        super.setTitle(getString("confirm-exit-title"));
        super.setHeaderText(getString("confirm-exit-header"));
    }

}
