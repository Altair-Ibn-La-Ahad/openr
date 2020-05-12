package pl.greywarden.openr.component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;

import java.util.List;

public class DirectoryTableView extends TableView<FilesystemEntryWrapper> {
    private final TableColumn<FilesystemEntryWrapper, String> nameColumn = new TableColumn<>();
    private final TableColumn<FilesystemEntryWrapper, String> extensionColumn = new TableColumn<>();
    private final TableColumn<FilesystemEntryWrapper, String> sizeColumn = new TableColumn<>();
    private final TableColumn<FilesystemEntryWrapper, String> modifiedColumn = new TableColumn<>();
    private final TableColumn<FilesystemEntryWrapper, String> privilegesColumn = new TableColumn<>();

    private final StringProperty pathProperty = new SimpleStringProperty();

    public DirectoryTableView() {
        super();
        initComponent();
    }

    private void initComponent() {
        createColumns();
        super.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        super.setPlaceholder(new Label());
    }

    @SuppressWarnings("unchecked")
    private void createColumns() {
        super.getColumns().setAll(nameColumn, extensionColumn, sizeColumn, modifiedColumn, privilegesColumn);
        nameColumn.setText("Name");
        extensionColumn.setText("Ext.");
        sizeColumn.setText("Size");
        modifiedColumn.setText("Modified");
        privilegesColumn.setText("Priv.");

        nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        extensionColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getExtension()));
        sizeColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSize()));
        modifiedColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getModified()));
        privilegesColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getPrivileges()));
    }

    public StringProperty pathProperty() {
        return this.pathProperty;
    }

    public void setData(List<FilesystemEntryWrapper> files) {
        Platform.runLater(() -> {
            this.getItems().clear();
            this.getItems().setAll(files);
        });
    }
}
