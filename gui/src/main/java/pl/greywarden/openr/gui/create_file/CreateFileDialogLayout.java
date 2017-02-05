package pl.greywarden.openr.gui.create_file;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.Getter;
import pl.greywarden.openr.commons.PathComboBox;
import pl.greywarden.openr.commons.TemplateComboBox;

import java.io.File;

import static pl.greywarden.openr.commons.I18nManager.getString;

public abstract class CreateFileDialogLayout extends GridPane {

    protected PathComboBox pathComboBox;
    protected static TemplateComboBox templates;

    @Getter
    protected final static TextField fileNameTextField = new TextField();
    private static final Label filenameLabel = new Label(getString("filename") + ":");
    private static final Label templateLabel = new Label(getString("type") + ":");
    private static final Label pathLabel = new Label(getString("path") + ":");

    protected CreateFileDialogLayout() {
        super();

        pathComboBox = new PathComboBox();
        templates = new TemplateComboBox();

        super.setHgap(10);
        super.setVgap(10);
        super.setPadding(new Insets(20, 10, 10, 10));

        fileNameTextField.setPromptText(getString("filename"));
        fileNameTextField.setText("");
    }

    protected void createGridLayout() {
        GridPane.setHgrow(fileNameTextField, Priority.ALWAYS);
        pathComboBox.minWidthProperty().bind(fileNameTextField.widthProperty());
        templates.minWidthProperty().bind(fileNameTextField.widthProperty());
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

    public File getTargetFile() {
        return new File(pathComboBox.getSelectedPath(), fileNameTextField.getText());
    }
}
