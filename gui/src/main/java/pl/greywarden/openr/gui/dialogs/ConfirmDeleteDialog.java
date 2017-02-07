package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.Alert;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.util.LinkedList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class ConfirmDeleteDialog extends Alert {

    public ConfirmDeleteDialog(DirectoryView directoryView) {
        super(AlertType.CONFIRMATION);

        super.setTitle(getString("confirm-delete-dialog-title"));
        super.setHeaderText(getString("confirm-delete-dialog-warning"));
        super.setGraphic(IconManager.getIcon("delete-permanent"));
        List<AbstractEntry> items = new LinkedList<>();
        directoryView.getSelectionModel().getSelectedItems()
                .forEach(entryWrapper -> items.add(entryWrapper.getEntry()));
        super.getDialogPane().getButtonTypes().setAll(CommonButtons.YES, CommonButtons.NO);
        super.showAndWait().ifPresent(buttonType -> {
            if (CommonButtons.YES.equals(buttonType)) {
                deleteSelectedItemsAndReloadView(directoryView, items);
            }
        });
    }

    private void deleteSelectedItemsAndReloadView(DirectoryView directoryView, List<AbstractEntry> items) {
        items.forEach(AbstractEntry::delete);
        directoryView.reload();
    }

}
