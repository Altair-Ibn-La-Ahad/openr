package pl.greywarden.openr.gui.dialogs;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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

import static pl.greywarden.openr.i18n.I18nManager.getString;

@Log4j
public class NewDirectoryDialog extends Dialog <ButtonType> {

    public NewDirectoryDialog(DirectoryView view) {
        GridPane layout = new GridPane();
        layout.setHgap(5);

        ButtonType ok = new ButtonType(getString("ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

        Label directoryNameLabel = new Label(getString("name") + ":");
        TextField directoryName = new TextField();
        GridPane.setHgrow(directoryName, Priority.ALWAYS);

        layout.addRow(0, directoryNameLabel, directoryName);
        super.setTitle(getString("new-directory-dialog-title"));
        super.getDialogPane().getButtonTypes().setAll(ok, cancel);
        super.getDialogPane().setContent(layout);
        Node buttonOk = super.getDialogPane().lookupButton(ok);
        directoryName.textProperty().addListener((observable, oldValue, newValue) -> {
            File target = new File(newValue);
            buttonOk.setDisable(newValue.trim().isEmpty() || target.exists());
        });
        buttonOk.setDisable(true);
        super.getDialogPane().setMinWidth(300);
        directoryName.requestFocus();
        super.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                File target = new File(view.getRootPath(), directoryName.getText());
                try {
                    Files.createDirectory(target.toPath());
                    view.reload();
                } catch (IOException e) {
                    log.error("Failed to create directory", e);
                }
            }
        });
    }

}
