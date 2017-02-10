package pl.greywarden.openr.gui.create_file;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class CreateFileDialog extends Dialog<ButtonType> {

    private final ButtonType create = CommonButtons.OK;
    private CreateFileDialogLayout layout;

    public CreateFileDialog() {
        super();
        super.setTitle(getString("new-file-dialog-title"));
        super.setHeaderText(getString("new-file-dialog-header"));
        setLayoutAndButtonTypes(new NewFileDialogLayout());
        showDialog();
    }

    public CreateFileDialog(Template template) {
        super();
        super.setTitle(getString("create-file"));
        super.setTitle(getString("create-file-" + template.getName()));
        setLayoutAndButtonTypes(new CreateFileFromTemplateDialogLayout(template));
        showDialog();
    }

    public CreateFileDialog(Template template, DirectoryView selectedView) {
        super();
        super.setTitle(getString("create-file"));
        super.setTitle(getString("create-file-" + template.getName()));
        setLayoutAndButtonTypes(new CreateFileFromTemplateDialogLayout(template, selectedView));
        showDialog();
    }

    private void setButtonTypes() {
        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK, CommonButtons.CANCEL);
        Node buttonOk = super.getDialogPane().lookupButton(create);
        BooleanBinding binding = CreateFileDialogLayout.fileNameTextField.textProperty().isNotEmpty();
        buttonOk.disableProperty().bind(binding.and(layout.pathComboBox.pathValidBinding.not()).not());
    }

    private void setLayoutAndButtonTypes(CreateFileDialogLayout layout) {
        this.layout = layout;
        setButtonTypes();
        super.getDialogPane().minWidthProperty().set(500);
        super.getDialogPane().setContent(layout);
        CreateFileDialogLayout.getFileNameTextField().requestFocus();
    }

    public void showDialog() {
        super.showAndWait().ifPresent(buttonType -> {
            if (okOptionSelected(buttonType)) {
                layout.handleConfirm();
            }
        });
    }

    private boolean okOptionSelected(ButtonType buttonType) {
        return CommonButtons.OK.equals(buttonType);
    }
}
