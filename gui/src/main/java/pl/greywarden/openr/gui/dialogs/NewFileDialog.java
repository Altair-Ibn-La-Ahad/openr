package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.Pair;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.util.function.Consumer;

import static pl.greywarden.openr.i18n.I18nManager.getString;

public class NewFileDialog extends CreateFileDialog {

    private ComboBox<Template> templates;

    public NewFileDialog(DirectoryView left, DirectoryView right) {
        super(null, left, right);
        createHeaderAndTitle();
    }

    @Override
    protected void createDialogContent() {
        super.createDialogContent();
        createTemplatesComboBox();
        getGrid().addRow(2, new Label(getString("type") + ":"), templates);
    }

    private void createTemplatesComboBox() {
        templates = new ComboBox<>();
        templates.getItems().addAll(Template.getAvailableTemplates());
        templates.setButtonCell(createButtonCell());
        templates.setCellFactory(param -> createButtonCell());
        templates.getSelectionModel().select(0);
        templates.setMinWidth(400);
    }

    private ListCell<Template> createButtonCell() {
        return new ListCell<Template>() {
            @Override
            protected void updateItem(Template t, boolean empty) {
                super.updateItem(t, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(getString(t.getName() + "-menu-item"));
                }
            }
        };
    }

    @Override
    protected Consumer<Pair<String, String>> confirmHandler() {
        return o -> {
            File target = new File(o.getKey(), o.getValue());
            templates.getSelectionModel().getSelectedItem().build(target.getAbsolutePath());
            if (pathComboBox.getSelectionModel().getSelectedIndex() == 0) {
                left.reload();
            } else {
                right.reload();
            }
            super.close();
        };
    }

    @Override
    protected void createHeaderAndTitle() {
        super.setTitle(getString("new-file-dialog-title"));
        super.setHeaderText(getString("new-file-dialog-header"));
    }
}
