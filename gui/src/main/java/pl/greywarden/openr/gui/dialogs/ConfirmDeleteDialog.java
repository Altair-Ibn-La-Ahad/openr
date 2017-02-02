package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class ConfirmDeleteDialog extends Alert {

    public ConfirmDeleteDialog(DirectoryView directoryView) {
        super(AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType(getString("yes"), ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(getString("no"), ButtonBar.ButtonData.NO);

        super.setTitle(getString("confirm-delete-dialog-title"));
        AbstractEntry selectedItem = directoryView.getSelectionModel().getSelectedItem().getEntry();
        super.setHeaderText(getString("confirm-delete-dialog-warning"));

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
