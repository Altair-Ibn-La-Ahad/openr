package pl.greywarden.openr.gui.property_editor;

import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringEscapeUtils;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.property_editor.table_view.PropertyWrapper;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class NewPropertyDialog extends Dialog <Boolean> {

    private TextField keyInput;
    private TextField valueInput;
    private final PropertyEditorDialog parent;

    public NewPropertyDialog(PropertyEditorDialog parent) {
        super();
        this.parent = parent;

        setOkCancelButtons();
        createLayout();
        super.initStyle(StageStyle.UTILITY);
        super.setResultConverter(param -> CommonButtons.OK.equals(param));
        disableConfirmOnInvalidData();
        keyInput.requestFocus();
        super.showAndWait().ifPresent(confirm -> {
            if (confirm) {
                handleConfirm();
            }
        });
    }

    private void handleConfirm() {
        String key = StringEscapeUtils.escapeJava(keyInput.getText());
        String value = StringEscapeUtils.escapeJava(valueInput.getText());
        parent.getTableView().getItems().add(new PropertyWrapper(key, value));
        parent.getTableView().editedProperty.set(true);
        parent.setTitleWithoutAsterisk();
        parent.propertiesEdited.invalidate();
    }

    private void createLayout() {
        GridPane layout = new GridPane();
        layout.setHgap(5);
        layout.setVgap(10);

        Label keyLabel = new Label(getString("key"));
        Label valueLabel = new Label(getString("value"));

        keyInput = new TextField();
        valueInput = new TextField();

        layout.addRow(0, keyLabel, keyInput);
        layout.addRow(1, valueLabel, valueInput);

        super.getDialogPane().setContent(layout);
    }

    private void setOkCancelButtons() {
        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK, CommonButtons.CANCEL);
    }

    private Node getOkButton() {
        return super.getDialogPane().lookupButton(CommonButtons.OK);
    }

    private void disableConfirmOnInvalidData() {
        getOkButton().setDisable(true);
        keyInput.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        getOkButton().disableProperty().setValue(
                                newValue == null || newValue.isEmpty()
                                        || parent.getTableView().containsKey(newValue)));
    }
}
