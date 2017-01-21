package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.Pair;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.util.function.Consumer;

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
        getGrid().addRow(2, new Label(i18n.getString("file-type") + ":"), templates);
    }

    private void createTemplatesComboBox() {
        templates = new ComboBox<>();
        templates.getItems().addAll(Template.getAvailableTemplates());
        templates.setButtonCell(new ListCell<Template>() {
            @Override
            protected void updateItem(Template t, boolean empty) {
                super.updateItem(t, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(i18n.getString(t.getName()));
                }
            }
        });
        templates.setCellFactory(param -> new ListCell<Template>() {
            @Override
            protected void updateItem(Template t, boolean empty) {
                super.updateItem(t, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(i18n.getString(t.getName()));
                }
            }
        });
        templates.getSelectionModel().select(0);
        templates.setMinWidth(400);
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
        i18n.setBundle("new-file-dialog");
        super.setTitle(i18n.getString("title"));
        super.setHeaderText(i18n.getString("header"));
    }
}
