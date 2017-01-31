package pl.greywarden.openr.gui.dialogs;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.io.File;

import static pl.greywarden.openr.i18n.I18nManager.getString;

@Log4j
public class RenameDialog extends Dialog <ButtonType> {

    public RenameDialog(DirectoryView directoryView) {
        GridPane layout = new GridPane();
        layout.setHgap(5);

        ButtonType ok = new ButtonType(getString("ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

        Label newNameLabel = new Label(getString("new-name") + ":");
        TextField newName = new TextField();

        AbstractEntry selectedItem = directoryView.getSelectionModel().getSelectedItem().getEntry();

        layout.addRow(0, newNameLabel, newName);
        super.setTitle(getString("rename-dialog-title"));
        super.getDialogPane().getButtonTypes().setAll(ok, cancel);
        super.getDialogPane().setContent(layout);
        Node buttonOk = super.getDialogPane().lookupButton(ok);
        newName.textProperty().addListener((observable, oldValue, newValue) -> {
            String extension = selectedItem.getEntryProperties().getExtension();
            if (!extension.isEmpty()) {
                extension = "." + extension;
            }
            File targetFile = new File(selectedItem.getEntryProperties().getParentFile(),
                    newValue + extension);
            buttonOk.setDisable(
                    newValue.trim().isEmpty() ||
                            targetFile.exists());
        });
        buttonOk.setDisable(true);
        newName.requestFocus();
        super.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                File sourceDir = selectedItem.getFilesystemEntry();
                File target = new File(selectedItem.getEntryProperties().getParentFile(), newName.getText());
                if (!sourceDir.renameTo(target)) {
                    log.error("Unable to rename file");
                }
                directoryView.reload();
            }
        });
    }

}
