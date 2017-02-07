package pl.greywarden.openr.gui.dialogs;

import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.io.File;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class RenameDialog extends Dialog <Boolean> {

    private final GridPane layout = new GridPane();
    private final Label newNameLabel = new Label(getString("new-name") + ":");
    private final TextField newName = new TextField();

    public RenameDialog(DirectoryView directoryView) {
        super.setTitle(getString("rename-dialog-title"));
        layout.setHgap(5);

        AbstractEntry selectedItem = getSelectedItem(directoryView);
        createNewNameLabelAndInput();
        createDialog();
        disableOkButtonWhenTargetExists(selectedItem);
        disableOkByDefault();
        newName.requestFocus();
        super.showAndWait().ifPresent(confirm -> {
            if (confirm) {
                handleRename(directoryView, selectedItem);
            }
        });
    }

    private AbstractEntry getSelectedItem(DirectoryView directoryView) {
        return directoryView.getSelectionModel().getSelectedItem().getEntry();
    }

    private void disableOkByDefault() {
        getOkButton().setDisable(true);
    }

    private void createDialog() {
        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK, CommonButtons.CANCEL);
        super.getDialogPane().setContent(layout);
        super.setResultConverter(CommonButtons.OK::equals);
    }

    private void disableOkButtonWhenTargetExists(AbstractEntry selectedItem) {
        newName.textProperty().addListener((observable, oldValue, newValue) -> {
            String extension = getEntryExtension(selectedItem);
            if (!extension.isEmpty()) {
                extension = "." + extension;
            }
            File targetFile = getTargetFile(selectedItem, newValue, extension);
            disableOkWhenTargetFileExists(newValue, targetFile);
        });
    }

    private void handleRename(DirectoryView directoryView, AbstractEntry selectedItem) {
        File source = selectedItem.getFilesystemEntry();
        File target = getTargetFile(selectedItem);
        rename(source, target);
        directoryView.reload();
    }

    private void rename(File source, File target) {
        if (!source.renameTo(target)) {
            log.error("Unable to rename file");
        }
    }

    private File getTargetFile(AbstractEntry selectedItem) {
        return new File(selectedItem.getEntryProperties().getParentFile(), newName.getText());
    }

    private void disableOkWhenTargetFileExists(String newValue, File targetFile) {
        getOkButton().setDisable(newValue.trim().isEmpty() || targetFile.exists());
    }

    private File getTargetFile(AbstractEntry selectedItem, String newValue, String extension) {
        return new File(selectedItem.getEntryProperties().getParentFile(), newValue + extension);
    }

    private String getEntryExtension(AbstractEntry selectedItem) {
        return selectedItem.getEntryProperties().getExtension();
    }

    private Node getOkButton() {
        return super.getDialogPane().lookupButton(CommonButtons.OK);
    }

    private void createNewNameLabelAndInput() {
        layout.addRow(0, newNameLabel, newName);
    }

}
