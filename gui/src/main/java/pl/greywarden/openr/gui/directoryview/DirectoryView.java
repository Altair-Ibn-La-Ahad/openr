package pl.greywarden.openr.gui.directoryview;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import lombok.Getter;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.i18n.I18nManager;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class DirectoryView extends TableView {

    private DirectoryEntry rootEntry;

    private final I18nManager i18n = I18nManager.getInstance();

    @Getter
    private String rootPath;

    public DirectoryView(String rootPath) {
        super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        build(rootPath);
    }

    public void changePath(String rootPath) {
        this.rootPath = rootPath;
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
        name.setCellFactory(param -> new DirectoryViewEntryNameCell());
        name.setComparator(nameComparator());

        extension.setCellValueFactory(new PropertyValueFactory<>("extension"));
        extension.setCellFactory(param -> new DirectoryViewEntryStringPropertyCell());
        extension.maxWidthProperty().set(150);
        extension.minWidthProperty().set(100);
        extension.setComparator(stringComparator());

        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        size.setCellFactory(param -> new DirectoryViewEntrySizePropertyCell());
        size.maxWidthProperty().set(150);
        size.minWidthProperty().set(100);
        size.setComparator(longComparator());

        modificationDate.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        modificationDate.maxWidthProperty().set(300);
        modificationDate.minWidthProperty().set(200);
        modificationDate.setCellFactory(param -> new DirectoryViewEntryDatePropertyCell());
        modificationDate.setComparator(dateComparator());

        privileges.setCellValueFactory(new PropertyValueFactory<>("privileges"));
        privileges.maxWidthProperty().set(200);
        privileges.minWidthProperty().set(150);
        privileges.setCellFactory(param -> new DirectoryViewEntryStringPropertyCell());
        privileges.setComparator(stringComparator());

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

    private Comparator<String> stringComparator(){
        return (o1, o2) -> {
            if ("..".equals(o1) || "..".equals(o2)) {
                return -1;
            }
            return o1.compareToIgnoreCase(o2);
        };
    }

    private Comparator<Date> dateComparator() {
        return (o1, o2) -> {
            if (o1.equals(new Date(Long.MIN_VALUE)) || o2.equals(new Date(Long.MIN_VALUE))) {
                return -1;
            }
            return o1.compareTo(o2);
        };
    }

    private Comparator<Long> longComparator() {
        return (o1, o2) -> {
            if (o1.equals(Long.MIN_VALUE) || o2.equals(Long.MIN_VALUE)) {
                return -1;
            }
            return o1.compareTo(o2);
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

}
