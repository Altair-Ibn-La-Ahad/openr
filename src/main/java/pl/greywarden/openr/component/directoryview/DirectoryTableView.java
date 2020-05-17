package pl.greywarden.openr.component.directoryview;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.greywarden.openr.component.directoryview.column.ExtensionColumn;
import pl.greywarden.openr.component.directoryview.column.NameColumn;
import pl.greywarden.openr.component.directoryview.column.SizeColumn;
import pl.greywarden.openr.domain.DirectoryContent;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;

import java.util.Comparator;

public class DirectoryTableView extends TableView<FilesystemEntryWrapper> {
    private final NameColumn nameColumn = new NameColumn();
    private final ExtensionColumn extensionColumn = new ExtensionColumn();
    private final SizeColumn sizeColumn = new SizeColumn();
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
        makeFirstRowAlwaysFirst();
    }

    public StringProperty pathProperty() {
        return this.pathProperty;
    }

    public void setData(DirectoryContent files) {
        Platform.runLater(() -> {
            nameColumn.hasParentProperty().setValue(files.hasParent());
            setItems(FXCollections.observableArrayList(files));
            refresh();
            sort();
        });
    }

    private void makeFirstRowAlwaysFirst() {
        super.sortPolicyProperty().set(table -> {
            FXCollections.sort(getItems(), getEntryComparator(table));
            return true;
        });
    }

    private Comparator<FilesystemEntryWrapper> getEntryComparator(TableView<FilesystemEntryWrapper> tableView) {
        return (e1, e2) -> {
            if (e1.isParentDirectoryEntry()) {
                return -1;
            } else if (e2.isParentDirectoryEntry()) {
                return 1;
            } else if (tableView.getComparator() == null) {
                return 0;
            }
            return tableView.getComparator().compare(e1, e2);
        };
    }

    @SuppressWarnings("unchecked")
    private void createColumns() {
        super.getColumns().setAll(nameColumn, extensionColumn, sizeColumn, modifiedColumn, privilegesColumn);
        nameColumn.setText("Name");
        extensionColumn.setText("Ext.");
        sizeColumn.setText("Size");
        modifiedColumn.setText("Modified");
        privilegesColumn.setText("Priv.");

        extensionColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getExtension()));
        modifiedColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getModified()));
        privilegesColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getPrivileges()));
    }
}
