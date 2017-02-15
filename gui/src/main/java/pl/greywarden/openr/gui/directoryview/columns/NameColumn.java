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
                    String nameOfEntry = item.getEntryProperties().getBaseName();
                    Label nameLabel = new Label(nameOfEntry);
                    ImageView icon = createIconForFilesystemEntry(item);
                    setGraphic(createLabelWrapper(nameLabel, icon));
                    Tooltip.install(this, new Tooltip(item.getEntryProperties().getName()));
                }
            }

            private HBox createLabelWrapper(Label nameLabel, ImageView iv) {
                HBox wrapper = new HBox();
                wrapper.setAlignment(Pos.CENTER_LEFT);
                wrapper.getChildren().addAll(iv, nameLabel);
                return wrapper;
            }

            private ImageView createIconForFilesystemEntry(AbstractEntry item) {
                ImageView icon = new ImageView(IconManager.getFileIconSmall(item.getEntryProperties().getAbsolutePath()));
                HBox.setMargin(icon, new Insets(0, 3, 0, (isItemParentDirectoryEntry(item) || hasParent(item)) ? 0 : 8));
                return icon;
            }

            private boolean hasParent(AbstractEntry item) {
                return item.getFilesystemEntry().getParentFile().getParentFile() == null;
            }
        });
        super.setComparator(nameComparator());
    }

    private boolean isItemParentDirectoryEntry(AbstractEntry item) {
        return item instanceof ParentDirectoryEntry;
    }

    private Comparator<AbstractEntry> nameComparator() {
        return (o1, o2) -> {
            String n1 = o1.getEntryProperties().getBaseName();
            String n2 = o2.getEntryProperties().getBaseName();
            if (isItemParentDirectoryEntry(o1) || isItemParentDirectoryEntry(o2)) {
                return -1;
            }
            return n1.compareToIgnoreCase(n2);
        };
    }
}