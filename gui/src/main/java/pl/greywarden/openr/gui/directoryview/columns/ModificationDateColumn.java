package pl.greywarden.openr.gui.directoryview.columns;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.greywarden.openr.filesystem.AbstractEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class ModificationDateColumn extends TableColumn<AbstractEntry, Date> {

    public ModificationDateColumn() {
        super(getString("directory-view-column-modification-date"));
        super.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        super.setCellFactory(param -> new TableCell<AbstractEntry, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Date veryLongTimeAgo = new Date(Long.MIN_VALUE);
                    DateFormat format = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
                    setText(item.equals(veryLongTimeAgo) ? "" : format.format(item));
                }
            }
        });
        super.setComparator(modificationDateComparator());
    }

    private Comparator<Date> modificationDateComparator() {
        return (o1, o2) -> {
            if (o1.equals(new Date(Long.MIN_VALUE)) || o2.equals(new Date(Long.MIN_VALUE))) {
                return -1;
            }
            return o1.compareTo(o2);
        };
    }
}
