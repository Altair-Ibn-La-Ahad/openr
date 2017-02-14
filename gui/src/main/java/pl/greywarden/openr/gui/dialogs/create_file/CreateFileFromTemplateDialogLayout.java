package pl.greywarden.openr.gui.dialogs.create_file;

import pl.greywarden.openr.commons.PathComboBox;
import pl.greywarden.openr.commons.TemplateComboBox;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

public class CreateFileFromTemplateDialogLayout extends CreateFileDialogLayout {

    public CreateFileFromTemplateDialogLayout(Template template) {
        super();
        templates = new TemplateComboBox(template);
        createGridLayout();
    }

    public CreateFileFromTemplateDialogLayout(Template template, DirectoryView selectedView) {
        super();
        templates = new TemplateComboBox(template);
        pathComboBox = new PathComboBox(selectedView);
        createGridLayout();
    }

    @Override
    public void handleConfirm() {
        Template template = templates.getSelectedTemplate();
        template.build(getTargetFile().getAbsolutePath());
        pathComboBox.reloadSelected();
    }
}
