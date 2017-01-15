package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import pl.greywarden.openr.filesystem.AbstractEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DirectoryViewEntryDatePropertyCell extends TableCell <AbstractEntry, Date> {

    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            HBox hb = new HBox();
            Date veryLongTimeAgo = new Date(Long.MIN_VALUE);
            DateFormat format = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
            Label label = new Label(item.equals(veryLongTimeAgo) ? "" : format.format(item));
            hb.getChildren().add(label);
            setGraphic(label);
        }
    }

}
