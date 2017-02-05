package pl.greywarden.openr.gui.directoryview.columns;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.greywarden.openr.filesystem.AbstractEntry;

import java.util.Comparator;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class PrivilegesColumn extends TableColumn<AbstractEntry, String> {

    public PrivilegesColumn() {
        super(getString("directory-view-column-privileges"));
        super.setCellValueFactory(new PropertyValueFactory<>("privileges"));
        super.setCellFactory(param -> new TableCell<AbstractEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText("..".equals(item) ? "" : item);
                }
            }
        });
        super.setComparator(privilegesComparator());
    }

    private Comparator<String> privilegesComparator() {
        return (o1, o2) -> {
            if ("..".equals(o1) || "..".equals(o2)) {
                return -1;
            }
            return o1.compareToIgnoreCase(o2);
        };
    }
}
