package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.gui.IconManager;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;

public class ConfirmDeleteDialog extends Alert {

    public ConfirmDeleteDialog(DirectoryView directoryView) {
        super(AlertType.CONFIRMATION);
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("confirm-delete");
        ButtonType yes = new ButtonType(i18n.getString("yes"), ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(i18n.getString("no"), ButtonBar.ButtonData.NO);

        super.setTitle(i18n.getString("delete"));
        AbstractEntry selectedItem = directoryView.getSelectionModel().getSelectedItem().getEntry();
        super.setHeaderText(i18n.getString("warning"));

        super.setGraphic(IconManager.getIcon("delete-permanent"));

        super.getDialogPane().getButtonTypes().setAll(yes, no);
        super.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                selectedItem.delete();
                directoryView.reload();
            }
        });
    }

}
