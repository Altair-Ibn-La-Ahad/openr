package pl.greywarden.openr.gui.directoryview.columns;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;

import java.util.Comparator;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class NameColumn extends TableColumn<AbstractEntry, AbstractEntry> {

    public NameColumn() {
        super(getString("directory-view-column-name"));
        super.setCellValueFactory(new PropertyValueFactory<>("entry"));
        super.setCellFactory(param -> new TableCell<AbstractEntry, AbstractEntry>() {
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
        });
        super.setComparator(nameComparator());
    }

    private Comparator<AbstractEntry> nameComparator() {
        return (o1, o2) -> {
            String n1 = o1.getEntryProperties().getBaseName();
            String n2 = o2.getEntryProperties().getBaseName();
            if ("..".equals(n1) || "..".equals(n2)) {
                return -1;
            }
            return n1.compareToIgnoreCase(n2);
        };
    }
}