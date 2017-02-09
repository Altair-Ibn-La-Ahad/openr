package pl.greywarden.openr.gui.property_editor.table_view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PropertiesTableView extends TableView<PropertyWrapper> {

    public final BooleanProperty editedProperty = new SimpleBooleanProperty(false);

    public PropertiesTableView() {
        super();
        super.setEditable(true);
        super.columnResizePolicyProperty().setValue(CONSTRAINED_RESIZE_POLICY);
        createColumns();
        super.placeholderProperty().setValue(new Label(""));
    }

    private void createColumns() {
        TableColumn key = new KeyColumn();
        TableColumn value = new ValueColumn();

        super.getColumns().setAll(key, value);
    }

    public boolean containsKey(String key) {
        for (PropertyWrapper pw : super.getItems()) {
            if (key.equals(pw.getKey())) {
                return true;
            }
        }
        return false;
    }
}
