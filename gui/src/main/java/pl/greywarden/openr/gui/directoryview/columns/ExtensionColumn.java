package pl.greywarden.openr.gui.directoryview.columns;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import pl.greywarden.openr.filesystem.AbstractEntry;

import java.util.Comparator;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class ExtensionColumn extends TableColumn<AbstractEntry, String> {

    public ExtensionColumn() {
        super(getString("directory-view-column-extension"));
        super.setCellValueFactory(new PropertyValueFactory<>("extension"));
        super.setCellFactory(extensionCellFactory());
        super.setComparator(extensionComparator());
    }

    private Callback<TableColumn<AbstractEntry, String>, TableCell<AbstractEntry, String>> extensionCellFactory() {
        return param -> new TableCell<AbstractEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText("..".equals(item) ? "" : item);
                }
            }
        };
    }

    private Comparator<String> extensionComparator() {
        return (o1, o2) -> {
            if ("..".equals(o1) || "..".equals(o2)) {
                return -1;
            }
            return o1.compareToIgnoreCase(o2);
        };
    }
}
