package pl.greywarden.openr.gui.directoryview.columns;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;

import java.util.Comparator;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class SizeColumn extends TableColumn<AbstractEntry, Long> {

    public SizeColumn() {
        super(getString("directory-view-column-size"));
        super.setCellValueFactory(new PropertyValueFactory<>("size"));
        super.setCellFactory(entrySizeCellFactory());
        super.setComparator(sizeComparator());
    }

    private Callback<TableColumn<AbstractEntry, Long>, TableCell<AbstractEntry, Long>> entrySizeCellFactory() {
        return param -> new TableCell<AbstractEntry, Long>() {
            @Override
            protected void updateItem(Long entrySize, boolean empty) {
                super.updateItem(entrySize, empty);
                if (!empty) {
                    setText(entrySize.equals(Long.MIN_VALUE) ? "" : FileUtils.byteCountToDisplaySize(entrySize));
                }
            }
        };
    }

    private Comparator<Long> sizeComparator() {
        return (o1, o2) -> {
            if (Long.MIN_VALUE == o1 || Long.MIN_VALUE == o2) {
                return -1;
            }
            return o1.compareTo(o2);
        };
    }
}
