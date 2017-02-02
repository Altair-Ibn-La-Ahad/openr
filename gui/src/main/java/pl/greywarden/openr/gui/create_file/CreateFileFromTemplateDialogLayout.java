package pl.greywarden.openr.gui.create_file;

import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.io.File;

public class CreateFileFromTemplateDialogLayout extends CreateFileDialogLayout {

    public CreateFileFromTemplateDialogLayout(Template template) {
        super();
        templates.getItems().setAll(template);
        templates.managedProperty().set(false);
        createGridLayout();
    }

    public CreateFileFromTemplateDialogLayout(Template template, DirectoryView selectedView) {
        super();
        templates.getItems().setAll(template);
        pathComboBox.getItems().setAll(selectedView);
        pathComboBox.getSelectionModel().select(0);
        templates.managedProperty().set(false);
        pathComboBox.managedProperty().set(false);
        createGridLayout();
    }

    @Override
    public void handleConfirm() {
        Template template = templates.getSelectionModel().getSelectedItem();
        File targetFile = new File(
                pathComboBox.getSelectionModel().getSelectedItem().getRootPath(),
                fileNameTextField.getText());
        template.build(targetFile.getAbsolutePath());
        pathComboBox.getSelectionModel().getSelectedItem().reload();
    }
}
