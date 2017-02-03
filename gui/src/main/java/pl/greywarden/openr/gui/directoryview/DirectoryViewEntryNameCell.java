package pl.greywarden.openr.gui.directoryview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;
import pl.greywarden.openr.commons.IconManager;

public class DirectoryViewEntryNameCell extends TableCell <AbstractEntry, AbstractEntry> {

    @Override
    protected void updateItem(AbstractEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            HBox vb = new HBox();
            vb.setAlignment(Pos.CENTER_LEFT);
            String nameOfEntry = item.getEntryProperties().getBaseName();
            Label nameLabel = new Label(nameOfEntry);
            ImageView iv = new ImageView(IconManager.getFileIcon(item.getEntryProperties().getAbsolutePath()));
            HBox.setMargin(iv, new Insets(0, 3, 0, (item instanceof ParentDirectoryEntry
                    || item.getFilesystemEntry().getParentFile().getParentFile() == null) ? 0 : 8));
            vb.getChildren().addAll(iv, nameLabel);
            setGraphic(vb);
            Tooltip.install(this, new Tooltip(item.getEntryProperties().getName()));
        }
    }

}
