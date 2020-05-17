package pl.greywarden.openr.component.directoryview.column;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import pl.greywarden.openr.component.LabelWithIcon;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;

public class NameColumn extends TableColumn<FilesystemEntryWrapper, String> {
    private final Insets labelPadding = new Insets(0, 0, 0, 8.0);
    private final BooleanProperty hasParentProperty = new SimpleBooleanProperty(true);

    public NameColumn() {
        super();
        initComponent();
    }

    private void initComponent() {
        super.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        super.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setText(null);
                } else {
                    var entry = getTableView().getItems().get(getIndex());
                    var cell = new LabelWithIcon(name, entry.getIcon());
                    if (!name.equals("..") && hasParentProperty.getValue()) {
                        cell.setPadding(labelPadding);
                    }
                    setGraphic(cell);
                }
            }
        });
        super.setComparator((n1, n2) -> {
            if ("..".equals(n1)) {
                return -1;
            }
            if ("..".equals(n2)) {
                return 1;
            }
            return n1.compareToIgnoreCase(n2);
        });
    }

    public BooleanProperty hasParentProperty() {
        return this.hasParentProperty;
    }
}
