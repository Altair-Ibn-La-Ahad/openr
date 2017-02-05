package pl.greywarden.openr.gui.create_file;

import pl.greywarden.openr.templates.Template;

public class NewFileDialogLayout extends CreateFileDialogLayout {

    public NewFileDialogLayout() {
        super();
        createGridLayout();
    }

    @Override
    public void handleConfirm() {
        Template template = templates.getSelectionModel().getSelectedItem();
        template.build(getTargetFile().getAbsolutePath());
        pathComboBox.reloadSelected();
    }
}
