package pl.greywarden.openr.gui.property_editor.table_view;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import pl.greywarden.openr.gui.property_editor.PropertyEditorDialog;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class KeyColumn extends TableColumn<PropertyWrapper, String> {

    public KeyColumn() {
        super(getString("key"));
        super.setCellValueFactory(new PropertyValueFactory<>("key"));
        super.setCellFactory(TextFieldTableCell.forTableColumn());
        super.setOnEditCommit(this::handleEditCommit);
        super.setComparator(String::compareToIgnoreCase);
    }

    private void handleEditCommit(CellEditEvent<PropertyWrapper, String> event) {
        PropertiesTableView table = (PropertiesTableView) event.getTableView();
        PropertyEditorDialog dialog = (PropertyEditorDialog) table.getParent().getScene().getWindow();
        int selectedRow = event.getTablePosition().getRow();
        String oldValue = event.getOldValue();
        String newValue = event.getNewValue();

        if (table.containsKey(newValue)) {
            table.getItems().get(selectedRow).setKey(oldValue);
            table.refresh();
        } else {
            table.getItems().get(selectedRow).setKey(newValue);
            table.editedProperty.setValue(true);
            dialog.addAsteriskAfterTitle();
        }
        dialog.propertiesEdited.invalidate();
    }
}
