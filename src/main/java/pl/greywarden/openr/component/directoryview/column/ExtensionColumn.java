package pl.greywarden.openr.component.directoryview.column;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;

public class ExtensionColumn extends TableColumn<FilesystemEntryWrapper, String> {

    public ExtensionColumn() {
        super();
        initComponent();
    }

    private void initComponent() {
        super.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getExtension()));
        super.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String extension, boolean empty) {
                super.updateItem(extension, empty);
                if (empty || "..".equals(extension)) {
                    setText(null);
                } else {
                    setText(extension);
                }
            }
        });
    }
}
