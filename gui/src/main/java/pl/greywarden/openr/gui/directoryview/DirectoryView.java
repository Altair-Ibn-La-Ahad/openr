package pl.greywarden.openr.gui.directoryview;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.gui.directoryview.columns.ExtensionColumn;
import pl.greywarden.openr.gui.directoryview.columns.ModificationDateColumn;
import pl.greywarden.openr.gui.directoryview.columns.NameColumn;
import pl.greywarden.openr.gui.directoryview.columns.PrivilegesColumn;
import pl.greywarden.openr.gui.directoryview.columns.SizeColumn;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Log4j
public class DirectoryView extends TableView<EntryWrapper> {

    private DirectoryEntry rootEntry;

    @Getter
    private String rootPath;

    private final TableColumn name = new NameColumn();
    private final TableColumn extension = new ExtensionColumn();
    private final TableColumn size = new SizeColumn();
    private final TableColumn modificationDate = new ModificationDateColumn();
    private final TableColumn privileges = new PrivilegesColumn();

    public DirectoryView(String rootPath) {
        build(rootPath);
        super.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                super.getSelectionModel().clearSelection();
            }
        });
        super.selectionModelProperty().get().setSelectionMode(SelectionMode.MULTIPLE);
        super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    }

    public void changePath(String rootPath) {
        this.rootPath = rootPath;
        rootEntry = new DirectoryEntry(rootPath);
        loadData();
    }

    private void build(String rootPath) {
        changePath(rootPath);
        createColumns();
    }

    private void loadData() {
        Task<Void> load = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DirectoryViewDataBuilder builder = new DirectoryViewDataBuilder(rootEntry);
                makeFirstRowAlwaysFirst();
                List<EntryWrapper> data = builder.getData();
                setItems(FXCollections.observableList(data));
                refresh();
                return null;
            }
        };
        Thread thread = new Thread(load);
        thread.setDaemon(true);
        thread.run();
    }

    private void makeFirstRowAlwaysFirst() {
        super.sortPolicyProperty().set(param -> {
            FXCollections.sort(getItems(), getWrapperComparator(param));
            return true;
        });
    }

    private Comparator<EntryWrapper> getWrapperComparator(TableView<EntryWrapper> param) {
        return (r1, r2) -> {
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
    }

    private void createColumns() {
        name.setResizable(true);
        super.getColumns().addAll(name, extension, size, modificationDate, privileges);
        setRowFactory();
    }

    @SuppressWarnings("unchecked")
    private void setRowFactory() {
        super.setRowFactory((TableView<EntryWrapper> tv) -> (TableRow<EntryWrapper>) createTableRow());
    }

    private Object createTableRow() {
        return new DirectoryViewTableRow(this);
    }

    public void reload() {
        changePath(rootPath);
    }

}
