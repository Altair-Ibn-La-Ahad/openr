package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.util.LinkedList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class ConfirmDeleteDialog extends Alert {

    public ConfirmDeleteDialog(DirectoryView directoryView) {
        super(AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType(getString("yes"), ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(getString("no"), ButtonBar.ButtonData.NO);

        super.setTitle(getString("confirm-delete-dialog-title"));
        super.setHeaderText(getString("confirm-delete-dialog-warning"));
        super.setGraphic(IconManager.getIcon("delete-permanent"));
        List<AbstractEntry> items = new LinkedList<>();
        directoryView.getSelectionModel().getSelectedItems()
                .forEach(entryWrapper -> items.add(entryWrapper.getEntry()));
        super.getDialogPane().getButtonTypes().setAll(yes, no);
        super.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                items.forEach(AbstractEntry::delete);
                directoryView.reload();
            }
        });
    }

}
