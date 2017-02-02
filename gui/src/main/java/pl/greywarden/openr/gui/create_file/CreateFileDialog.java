package pl.greywarden.openr.gui.create_file;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.util.Optional;

public class CreateFileDialog extends Dialog<ButtonType> {

    private final ButtonType create = CommonButtons.OK;
    private final ButtonType cancel = CommonButtons.CANCEL;
    private CreateFileDialogLayout layout;

    public CreateFileDialog() {
        super();
        setLayoutAndButtonTypes(new NewFileDialogLayout());
    }

    public CreateFileDialog(Template template) {
        super();
        setLayoutAndButtonTypes(new CreateFileFromTemplateDialogLayout(template));
    }

    public CreateFileDialog(Template template, DirectoryView selectedView) {
        super();
        setLayoutAndButtonTypes(new CreateFileFromTemplateDialogLayout(template, selectedView));
    }

    private void setButtonTypes() {
        super.getDialogPane().getButtonTypes().setAll(create, cancel);
        Node buttonOk = super.getDialogPane().lookupButton(create);
        buttonOk.disableProperty().bind(CreateFileDialogLayout.getFileNameTextField().textProperty().isEmpty());
    }

    private void setLayoutAndButtonTypes(CreateFileDialogLayout layout) {
        this.layout = layout;
        setButtonTypes();
        super.getDialogPane().setContent(layout);
        CreateFileDialogLayout.getFileNameTextField().requestFocus();
    }

    public void showDialog() {
        Optional<ButtonType> result = super.showAndWait();
        result.ifPresent(buttonType -> {
            if (ButtonBar.ButtonData.OK_DONE.equals(buttonType.getButtonData())) {
                layout.handleConfirm();
            }
        });
    }
}
