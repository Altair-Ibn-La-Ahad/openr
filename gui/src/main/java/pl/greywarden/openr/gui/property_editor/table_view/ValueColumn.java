package pl.greywarden.openr.gui.property_editor.table_view;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import org.apache.commons.lang3.StringEscapeUtils;
import pl.greywarden.openr.gui.property_editor.PropertyEditorDialog;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class ValueColumn extends TableColumn<PropertyWrapper, String> {

    public ValueColumn() {
        super(getString("value"));
        super.setCellValueFactory(new PropertyValueFactory<>("value"));
        super.setCellFactory(param -> new TextFieldTableCell<PropertyWrapper, String>(new DefaultStringConverter()) {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(StringEscapeUtils.unescapeJava(item));
                }
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(StringEscapeUtils.escapeJava(newValue));
            }

        });
        super.setOnEditCommit(event -> {
            int selectedRow = event.getTablePosition().getRow();
            String oldValue = event.getOldValue();
            String newValue = event.getNewValue();
            if (!newValue.equals(oldValue)) {
                event.getTableView().getItems().get(selectedRow).setValue(newValue);
                PropertyEditorDialog dialog = (PropertyEditorDialog) event.getTableView().getParent().getScene().getWindow();
                dialog.addAsteriskAfterTitle();
            }
        });
        super.setComparator(String::compareToIgnoreCase);
    }
}
