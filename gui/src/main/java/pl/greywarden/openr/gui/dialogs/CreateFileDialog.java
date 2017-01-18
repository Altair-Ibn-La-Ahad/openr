package pl.greywarden.openr.gui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.io.File;
import java.util.Optional;


public class CreateFileDialog extends Dialog<Pair<String, String>> {

    @SuppressWarnings("unchecked")
    public CreateFileDialog(Template template, DirectoryView left, DirectoryView right) {
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("create-file-dialog");

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

        grid.add(new Label(i18n.getString("filename") + ":"), 0, 0);
        grid.add(filename, 1, 0);
        grid.add(new Label(i18n.getString("path") + ":"), 0, 1);
        grid.add(pathComboBox, 1, 1);

        ColumnConstraints stretch = new ColumnConstraints();
        stretch.setFillWidth(true);
        grid.getColumnConstraints().addAll(stretch, stretch);

        super.getDialogPane().setContent(grid);
        Optional result = showAndWait();
        result.ifPresent(o -> {
            File target = new File(pathComboBox.getSelectionModel().getSelectedItem(), filename.getText());
            template.build(target.getAbsolutePath());
            if (pathComboBox.getSelectionModel().getSelectedIndex() == 0) {
                left.reload();
            } else {
                right.reload();
            }
            super.close();
        });
    }




}
