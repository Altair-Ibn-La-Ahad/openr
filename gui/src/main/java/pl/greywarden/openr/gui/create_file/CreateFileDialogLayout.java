package pl.greywarden.openr.gui.create_file;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;
import pl.greywarden.openr.templates.Template;

import static pl.greywarden.openr.commons.I18nManager.getString;

public abstract class CreateFileDialogLayout extends GridPane {

    protected static final ComboBox<DirectoryView> pathComboBox = new ComboBox<>();
    protected static final ComboBox<Template> templates = new ComboBox<>();

    @Getter
    protected static final TextField fileNameTextField = new TextField();
    private static final Label filenameLabel = new Label(getString("filename") + ":");
    private static final Label templateLabel = new Label(getString("type") + ":");
    private static final Label pathLabel = new Label(getString("path") + ":");
    private static final DirectoryView leftDirectoryView = MainWindow.getLeftDirectoryView();
    private static final DirectoryView rightDirectoryView = MainWindow.getRightDirectoryView();

    static {
        templates.getItems().addAll(Template.getAvailableTemplates());

        templates.setButtonCell(templateComboBoxButtonCell());
        templates.setCellFactory(param -> templateComboBoxButtonCell());

        pathComboBox.setButtonCell(directoryViewPathButtonCell());
        pathComboBox.setCellFactory(param -> directoryViewPathButtonCell());

        fileNameTextField.setPromptText(getString("filename"));
    }

    public CreateFileDialogLayout() {
        super();

        super.setHgap(10);
        super.setVgap(10);
        super.setPadding(new Insets(20, 10, 10, 10));

        ColumnConstraints stretch = new ColumnConstraints();
        stretch.setFillWidth(true);
        super.getColumnConstraints().addAll(stretch, stretch);

        if (leftDirectoryView.isVisible()) {
            if (rightDirectoryView.isVisible()) {
                pathComboBox.getItems().setAll(leftDirectoryView, rightDirectoryView);
            } else {
                pathComboBox.getItems().setAll(leftDirectoryView);
            }
        }
        if (rightDirectoryView.isVisible()) {
            if (!leftDirectoryView.isVisible()) {
                pathComboBox.getItems().setAll(rightDirectoryView);
            }
        }

        pathComboBox.getSelectionModel().select(0);
        templates.getSelectionModel().select(0);

        fileNameTextField.setText("");
    }

    private static ListCell<Template> templateComboBoxButtonCell() {
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

    private static ListCell<DirectoryView> directoryViewPathButtonCell() {
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

    protected void createGridLayout() {
        int rows = 0;
        super.addRow(0, filenameLabel, fileNameTextField);
        if (pathComboBox.managedProperty().get()) {
            super.addRow(++rows, pathLabel, pathComboBox);
        }
        if (templates.managedProperty().get()) {
            super.addRow(++rows, templateLabel, templates);
        }
    }

    public abstract void handleConfirm();
}
