package pl.greywarden.openr.gui.directoryview.columns;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;

import java.util.Comparator;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class SizeColumn extends TableColumn<AbstractEntry, Long> {

    public SizeColumn() {
        super(getString("directory-view-column-size"));
        super.setCellValueFactory(new PropertyValueFactory<>("size"));
        super.setCellFactory(param -> new TableCell<AbstractEntry, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.equals(Long.MIN_VALUE) ? "" : FileUtils.byteCountToDisplaySize(item));
                }
            }
        });
        super.setComparator(sizeComparator());
    }

    private Comparator<Long> sizeComparator() {
        return (o1, o2) -> {
            if (o1.equals(Long.MIN_VALUE) || o2.equals(Long.MIN_VALUE)) {
                return -1;
            }
            return o1.compareTo(o2);
        };
    }
}
