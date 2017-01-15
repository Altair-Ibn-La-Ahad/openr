package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;

public class DirectoryViewEntrySizePropertyCell extends TableCell<AbstractEntry, Long> {

    @Override
    protected void updateItem(Long item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            HBox hb = new HBox();
            Label label = new Label(item.equals(Long.MIN_VALUE) ? "" : FileUtils.byteCountToDisplaySize(item));
            hb.getChildren().add(label);
            setGraphic(label);
        }
    }

}
