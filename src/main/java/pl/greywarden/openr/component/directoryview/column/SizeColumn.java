package pl.greywarden.openr.component.directoryview.column;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;

import java.math.BigInteger;

public class SizeColumn extends TableColumn<FilesystemEntryWrapper, BigInteger> {

    public SizeColumn() {
        super();
        initComponent();
    }

    private void initComponent() {
        super.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getSize()));
        super.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigInteger size, boolean empty) {
                super.updateItem(size, empty);
                if (empty || size == null || size.longValue() == Long.MIN_VALUE) {
                    setText(null);
                } else {
                    setText(FileUtils.byteCountToDisplaySize(size));
                }
            }
        });
        super.setComparator(BigInteger::compareTo);
    }
}
