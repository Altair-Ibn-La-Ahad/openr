package pl.greywarden.openr.gui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import lombok.Getter;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;


public class CreateFileDialog extends Dialog<Pair<String, String>> {

    @Getter
    private GridPane grid;
    protected final I18nManager i18n = I18nManager.getInstance();
    protected ComboBox<String> pathComboBox;
    private TextField filename;
    private ButtonType create;

    protected final DirectoryView left;
    protected final DirectoryView right;
    private final Template template;

    @SuppressWarnings("unchecked")
    public CreateFileDialog(Template template, DirectoryView left, DirectoryView right) {
        this.left = left;
        this.right = right;
        this.template = template;

        createHeaderAndTitle();
        createGridPane();
        createDialogContent();
        createOkCancelOptions();
        setLayout();

        super.getDialogPane().setContent(grid);

        showDialog();
    }

    private void showDialog() {
        Optional<Pair<String, String>> result = showAndWait();
        result.ifPresent(confirmHandler());
    }

    private void createGridPane() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));
    }

    private void createOkCancelOptions() {
        create = new ButtonType(i18n.getString("create"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(i18n.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

        super.getDialogPane().getButtonTypes().addAll(create, cancel);
        super.setResultConverter(createResultConverter());

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

    protected void createDialogContent() {
        filename = new TextField();
        filename.setPromptText(i18n.getString("filename"));
        Label pathLabel = new Label(i18n.getString("path") + ":");
        pathComboBox = new ComboBox<>();
        if (right == null) {
            pathComboBox.getItems().setAll(left.getRootPath());
            pathComboBox.setVisible(false);
        } else {
            pathComboBox.getItems().setAll(left.getRootPath(), right.getRootPath());
            grid.add(pathLabel, 0, 1);
            grid.add(pathComboBox, 1, 1);
        }
        pathComboBox.getSelectionModel().select(0);

        grid.add(new Label(i18n.getString("filename") + ":"), 0, 0);
        grid.add(filename, 1, 0);

        filename.setMinWidth(400);
        pathComboBox.setMinWidth(400);
    }

    protected void createHeaderAndTitle() {
        i18n.setBundle("create-file-dialog");
        super.setTitle(i18n.getString(template.getName()));
        super.setHeaderText(i18n.getString("header"));
    }

    private Callback<ButtonType, Pair<String, String>> createResultConverter() {
        return param -> {
            if (param == create) {
                File target = new File(
                        pathComboBox.getSelectionModel().getSelectedItem(),
                        filename.getText());
                if (target.exists()) {
                    return null;
                }
                return new Pair<>(pathComboBox.getSelectionModel().getSelectedItem(), filename.getText());
            }
            return null;
        };
    }

    protected Consumer<Pair<String, String>> confirmHandler() {
        return o -> {
            File target = new File(o.getKey(), o.getValue());
            template.build(target.getAbsolutePath());
            if (pathComboBox.getSelectionModel().getSelectedIndex() == 0) {
                left.reload();
            } else {
                right.reload();
            }
            super.close();
        };
    }
}
