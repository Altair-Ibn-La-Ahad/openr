package pl.greywarden.openr.gui.dialogs;

import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class NewDirectoryDialog extends Dialog <Boolean> {

    private final GridPane layout = new GridPane();
    private final TextField directoryName = new TextField();
    private final Label directoryNameLabel = new Label(getString("name") + ":");

    public NewDirectoryDialog(DirectoryView view) {
        super.setTitle(getString("new-directory-dialog-title"));
        layout.setHgap(5);
        createDirectoryNameLabelAndInput();
        createDialog();
        disableConfirmIftargetExists();
        disableOkButtonByDefault();
        directoryName.requestFocus();
        super.showAndWait().ifPresent(confirm -> {
            if (confirm) {
                createDirectory(view, directoryName);
            }
        });
    }

    private void disableOkButtonByDefault() {
        getOkButton().setDisable(true);
    }

    private void createDialog() {
        super.setResultConverter(CommonButtons.OK::equals);
        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK, CommonButtons.CANCEL);
        super.getDialogPane().setContent(layout);
        super.getDialogPane().setMinWidth(300);
    }

    private void createDirectoryNameLabelAndInput() {
        GridPane.setHgrow(directoryName, Priority.ALWAYS);
        layout.addRow(0, directoryNameLabel, directoryName);
    }

    private Node getOkButton() {
        return super.getDialogPane().lookupButton(CommonButtons.OK);
    }

    private void disableConfirmIftargetExists() {
        directoryName.textProperty().addListener((observable, oldValue, newValue) -> {
            File target = new File(newValue);
            getOkButton().setDisable(newValue.trim().isEmpty() || target.exists());
        });
    }

    private void createDirectory(DirectoryView view, TextField directoryName) {
        File target = new File(view.getRootPath(), directoryName.getText());
        try {
            Files.createDirectory(target.toPath());
            view.reload();
        } catch (IOException e) {
            log.error("Failed to create directory", e);
        }
    }

}
