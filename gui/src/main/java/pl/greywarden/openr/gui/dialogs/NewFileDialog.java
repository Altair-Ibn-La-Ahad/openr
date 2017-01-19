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
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import javafx.util.StringConverter;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.util.Optional;

public class NewFileDialog extends Dialog<Pair<String, String>> {

    public NewFileDialog(DirectoryView left, DirectoryView right) {
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("new-file-dialog");
        super.setTitle(i18n.getString("title"));
        super.setHeaderText(i18n.getString("header"));

        ButtonType create = new ButtonType(i18n.getString("create"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(i18n.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        super.getDialogPane().getButtonTypes().addAll(create, cancel);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField filename = new TextField();
        filename.setPromptText(i18n.getString("filename"));

        ComboBox<String> pathComboBox = new ComboBox<>();
        pathComboBox.getItems().addAll(left.getRootPath(), right.getRootPath());
        pathComboBox.getSelectionModel().select(0);
        pathComboBox.setPrefWidth(400);

        ComboBox<Template> templates = new ComboBox<>();
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

        grid.addRow(0, new Label(i18n.getString("filename") + ":"), filename);
        grid.addRow(1, new Label(i18n.getString("path") + ":"), pathComboBox);
        grid.addRow(2, new Label(i18n.getString("file-type") + ":"), templates);

        ColumnConstraints stretch = new ColumnConstraints();
        stretch.setFillWidth(true);
        grid.getColumnConstraints().addAll(stretch);
        GridPane.setFillWidth(templates, true);


        super.getDialogPane().setContent(grid);

        templates.getSelectionModel().select(0);
        templates.setPrefWidth(400);

        super.setResultConverter(dialogButton -> {
            if (dialogButton == create) {
                File target = new File(
                        pathComboBox.getSelectionModel().getSelectedItem(),
                        filename.getText());
                if (target.exists()) {
                    return null;
                }
                return new Pair<>(pathComboBox.getSelectionModel().getSelectedItem(), filename.getText());
            }
            return null;
        });

        Node createButton = super.getDialogPane().lookupButton(create);
        filename.textProperty().addListener((observable, oldValue, newValue) ->
                createButton.setDisable(newValue.trim().isEmpty()));
        createButton.setDisable(true);
        filename.requestFocus();
        showAndWait().ifPresent(o -> {
            File target = new File(o.getKey(), o.getValue());
            templates.getSelectionModel().getSelectedItem().build(target.getAbsolutePath());
            if (pathComboBox.getSelectionModel().getSelectedIndex() == 0) {
                left.reload();
            } else {
                right.reload();
            }
            super.close();
        });
    }

}
