package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import pl.greywarden.openr.filesystem.AbstractEntry;

public class DirectoryViewEntryStringPropertyCell extends TableCell <AbstractEntry, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            HBox hb = new HBox();
            Label label = new Label("..".equals(item) ? "" : item);
            hb.getChildren().add(label);
            setGraphic(label);
        }
    }
}
