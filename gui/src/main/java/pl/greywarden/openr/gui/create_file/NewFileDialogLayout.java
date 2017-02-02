package pl.greywarden.openr.gui.create_file;

import pl.greywarden.openr.templates.Template;

import java.io.File;

public class NewFileDialogLayout extends CreateFileDialogLayout {

    public NewFileDialogLayout() {
        super();
        createGridLayout();
    }

    @Override
    public void handleConfirm() {
        String rootPath = pathComboBox.getSelectionModel().getSelectedItem().getRootPath();
        String fileName = fileNameTextField.getText();
        Template template = templates.getSelectionModel().getSelectedItem();
        File targetFile = new File(rootPath, fileName);
        template.build(targetFile.getAbsolutePath());
        pathComboBox.getSelectionModel().getSelectedItem().reload();
    }
}
