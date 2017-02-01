package pl.greywarden.openr.gui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import static pl.greywarden.openr.i18n.I18nManager.getString;

public class CreateFileDialog extends Dialog<ButtonType> {

    @Getter
    private GridPane grid;
    private ComboBox<DirectoryView> pathComboBox;
    private TextField filename;
    private ButtonType create;
    private ComboBox<Template> templates;

    private final DirectoryView left;
    private final DirectoryView right;
    private final Template template;
    private Consumer<ButtonType> confirmHandler;

    @SuppressWarnings("unchecked")
    public CreateFileDialog(Template template, DirectoryView left, DirectoryView right) {
        this.left = left;
        this.right = right;
        this.template = template;

        if (template != null) {
            super.setTitle(getString("create-file-" + template.getName()));
            super.setHeaderText(getString("create-file-header"));
        }

        createGridPane();
        createDialogContent();
        createOkCancelOptions();
        setLayout();

        super.getDialogPane().setContent(grid);

        filename.requestFocus();
        confirmHandler = createFileFromTemplateConfirmHandler();
        pathComboBox.setCellFactory(param -> createButtonCellForDirectoryViewComboBox());
        pathComboBox.setButtonCell(createButtonCellForDirectoryViewComboBox());
    }

    public CreateFileDialog(DirectoryView left, DirectoryView right) {
        this(null, left, right);
        createTemplatesComboBox();
        super.setTitle(getString("new-file-dialog-title"));
        super.setHeaderText(getString("new-file-dialog-header"));
        getGrid().addRow(2, new Label(getString("type") + ":"), templates);
        confirmHandler = createNewFileConfirmHandler();
    }

    public void showDialog() {
        Optional<ButtonType> result = showAndWait();
        result.ifPresent(buttonType -> confirmHandler.accept(buttonType));
    }

    private void createGridPane() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));
    }

    private void createOkCancelOptions() {
        create = new ButtonType(getString("ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

        super.getDialogPane().getButtonTypes().addAll(create, cancel);

        Node createButton = super.getDialogPane().lookupButton(create);
        filename.textProperty().addListener((observable, oldValue, newValue) ->
                createButton.setDisable(newValue.trim().isEmpty()));
        createButton.setDisable(true);
        filename.requestFocus();
    }

    private void setLayout() {
        ColumnConstraints stretch = new ColumnConstraints();
        stretch.setFillWidth(true);
        grid.getColumnConstraints().addAll(stretch, stretch);
    }

    private void createDialogContent() {
        filename = new TextField();
        filename.setPromptText(getString("filename"));
        Label pathLabel = new Label(getString("path") + ":");
        pathComboBox = new ComboBox<>();
        if (right == null || !right.isVisible()) {
            pathComboBox.getItems().setAll(left);
            pathComboBox.setVisible(false);
            pathComboBox.managedProperty().setValue(false);
        } else {
            pathComboBox.getItems().setAll(left, right);
            grid.add(pathLabel, 0, 1);
            grid.add(pathComboBox, 1, 1);
        }
        pathComboBox.getSelectionModel().select(0);

        grid.add(new Label(getString("filename") + ":"), 0, 0);
        grid.add(filename, 1, 0);

        filename.setMinWidth(400);
        pathComboBox.setMinWidth(400);
    }

    private void createTemplatesComboBox() {
        templates = new ComboBox<>();
        templates.getItems().addAll(Template.getAvailableTemplates());
        templates.setButtonCell(createButtonCellForTemplatesComboBox());
        templates.setCellFactory(param -> createButtonCellForTemplatesComboBox());
        templates.getSelectionModel().select(0);
        templates.setMinWidth(400);
    }

    private ListCell<Template> createButtonCellForTemplatesComboBox() {
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

    private ListCell<DirectoryView> createButtonCellForDirectoryViewComboBox() {
        return new ListCell<DirectoryView>() {
            @Override
            protected void updateItem(DirectoryView dv, boolean empty) {
                super.updateItem(dv, empty);
                if (!empty) {
                    setText(dv.getRootPath());
                }
            }
        };
    }

    private Consumer<ButtonType> createFileFromTemplateConfirmHandler() {
        return o -> {
            if (ButtonBar.ButtonData.OK_DONE.equals(o.getButtonData())) {
                File target = new File(
                        pathComboBox.getSelectionModel().getSelectedItem().getRootPath(),
                        filename.getText());
                template.build(target.getAbsolutePath());
                pathComboBox.getSelectionModel().getSelectedItem().reload();
                super.close();
            }
        };
    }

    private Consumer<ButtonType> createNewFileConfirmHandler() {
        return o -> {
            if (ButtonBar.ButtonData.OK_DONE.equals(o.getButtonData())) {
                File target = new File(
                        pathComboBox.getSelectionModel().getSelectedItem().getRootPath(),
                        filename.getText());
                templates.getSelectionModel().getSelectedItem().build(target.getAbsolutePath());
                pathComboBox.getSelectionModel().getSelectedItem().reload();
                super.close();
            }
        };
    }
}
