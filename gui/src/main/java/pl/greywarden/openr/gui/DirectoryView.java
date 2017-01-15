package pl.greywarden.openr.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.i18n.I18nManager;

import java.util.Comparator;
import java.util.Objects;

public class DirectoryView extends TableView {

    private DirectoryEntry rootEntry;

    private final I18nManager i18n = I18nManager.getInstance();

    public DirectoryView(String rootPath) {
        build(rootPath);
    }

    public void changePath(String rootPath) {
        rootEntry = new DirectoryEntry(rootPath);
        loadData();
    }

    private void build(String rootPath) {
        changePath(rootPath);
        i18n.setBundle("directory-view");
        createColumns();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        DirectoryViewDataBuilder builder = new DirectoryViewDataBuilder(rootEntry);
        makeFirstRowAlwaysFirst();
        super.setItems(builder.getData());
        super.refresh();
    }

    @SuppressWarnings("unchecked")
    private void makeFirstRowAlwaysFirst() {
        super.sortPolicyProperty().set((Callback<TableView<EntryWrapper>, Boolean>) param -> {
            Comparator<EntryWrapper> comparator1 = (r1, r2) -> {
                if (Objects.equals(r1.getName(), "..")) {
                    return -1;
                } else if (Objects.equals(r2.getName(), "..")) {
                    return 1;
                } else if (param.getComparator() == null) {
                    return 0;
                } else {
                    return param.getComparator().compare(r1, r2);
                }
            };
            FXCollections.sort(getItems(), comparator1);
            return true;
        });
    }

    @SuppressWarnings("unchecked")
    private void createColumns() {
        final TableColumn
                name = new TableColumn(i18n.getString("name")),
                extension = new TableColumn(i18n.getString("extension")),
                size = new TableColumn(i18n.getString("size")),
                modificationDate = new TableColumn(i18n.getString("modification-date")),
                privileges = new TableColumn(i18n.getString("privileges"));

        name.setCellValueFactory(new PropertyValueFactory<>("entry"));
        name.setCellFactory(param -> createEntryNameCell());
        name.setComparator(nameComparator());
        extension.setCellValueFactory(new PropertyValueFactory<>("extension"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        modificationDate.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        privileges.setCellValueFactory(new PropertyValueFactory<>("privileges"));
        super.getColumns().addAll(name, extension, size, modificationDate, privileges);
        setRowFactory();
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

    @SuppressWarnings("unchecked")
    private void setRowFactory() {
        super.setRowFactory(tv -> createTableRow());
    }

    private Object createTableRow() {
        TableRow<EntryWrapper> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!row.isEmpty())) {
                EntryWrapper rowData = row.getItem();
                if (rowData.getEntry().getEntryProperties().isDirectory()) {
                    TableViewSelectionModel model = super.getSelectionModel();
                    model.setSelectionMode(null);
                    changePath(rowData.getEntry().getEntryProperties().getAbsolutePath());
                }
            }
        });
        return row;
    }

    private TableCell createEntryNameCell() {
        return new DirectoryViewEntryNameCell();
    }

}
