package pl.greywarden.openr.gui.directoryview;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;

import javafx.scene.input.TransferMode;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class DirectoryView extends TableView<EntryWrapper> {

    private DirectoryEntry rootEntry;

    @Getter
    private String rootPath;

    public DirectoryView(String rootPath) {
        super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        build(rootPath);
        super.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                super.getSelectionModel().clearSelection();
            }
        });
        super.selectionModelProperty().get().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void changePath(String rootPath) {
        this.rootPath = rootPath;
        rootEntry = new DirectoryEntry(rootPath);
        Platform.runLater(this::loadData);
    }

    private void build(String rootPath) {
        changePath(rootPath);
        createColumns();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        DirectoryViewDataBuilder builder = new DirectoryViewDataBuilder(rootEntry);
        makeFirstRowAlwaysFirst();
        List<EntryWrapper> data = builder.getData();
        super.setItems(FXCollections.observableList(data));
        super.refresh();
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    private void createColumns() {
        final TableColumn
                name = new TableColumn(getString("directory-view-column-name")),
                extension = new TableColumn(getString("directory-view-column-extension")),
                size = new TableColumn(getString("directory-view-column-size")),
                modificationDate = new TableColumn(getString("directory-view-column-modification-date")),
                privileges = new TableColumn(getString("directory-view-column-privileges"));

        name.setCellValueFactory(new PropertyValueFactory<>("entry"));
        name.setCellFactory(param -> new DirectoryViewEntryNameCell());
        name.setComparator(nameComparator());
        name.minWidthProperty().bind(super.widthProperty().multiply(0.3));

        extension.setCellValueFactory(new PropertyValueFactory<>("extension"));
        extension.setCellFactory(param -> new DirectoryViewEntryStringPropertyCell());
        extension.setComparator(stringComparator());

        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        size.setCellFactory(param -> new DirectoryViewEntrySizePropertyCell());
        size.setComparator(longComparator());

        modificationDate.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        modificationDate.setCellFactory(param -> new DirectoryViewEntryDatePropertyCell());
        modificationDate.minWidthProperty().bind(super.widthProperty().multiply((1 - 0.3) * 0.08));
        modificationDate.setComparator(dateComparator());

        privileges.setCellValueFactory(new PropertyValueFactory<>("privileges"));
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

    private Comparator<String> stringComparator() {
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
        super.setRowFactory((TableView<EntryWrapper> tv) -> (TableRow<EntryWrapper>) createTableRow());
    }

    private Object createTableRow() {
        TableRow<EntryWrapper> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (row.isEmpty()) {
                super.getSelectionModel().clearSelection();
            }
            if (MouseButton.PRIMARY.equals(event.getButton())) {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EntryWrapper rowData = row.getItem();
                    if (rowData.getEntry().getEntryProperties().isDirectory()) {
                        TableViewSelectionModel model = super.getSelectionModel();
                        model.setSelectionMode(null);
                        changePath(rowData.getEntry().getEntryProperties().getAbsolutePath());
                    } else {
                        if (Desktop.isDesktopSupported()) {
                            File target = new File(rowData.getEntry().getEntryProperties().getAbsolutePath());
                            new Thread(() -> {
                                try {
                                    Desktop.getDesktop().browse(target.toURI());
                                } catch (IOException exception) {
                                    log.error("Unable to open selected file", exception);
                                }
                            }).start();
                        }
                    }
                }
            } else if (MouseButton.SECONDARY.equals(event.getButton())) {
                if (row.isEmpty()) {
                    new NoSelectionContextMenu(this).show(row, event.getScreenX(), event.getScreenY());
                } else {
                    if (super.getSelectionModel().getSelectedItems().size() == 1) {
                        EntryWrapper rowData = row.getItem();
                        AbstractEntry target = rowData.getEntry();
                        if (!(target instanceof ParentDirectoryEntry)) {
                            new SingleSelectionContextMenu(this, target)
                                    .show(row, event.getScreenX(), event.getScreenY());
                        }
                    }
                }
            }
        });
        row.setOnDragDetected(event -> {
            if (!row.isEmpty()) {
                List<EntryWrapper> selectedEntries = super.getSelectionModel().getSelectedItems();
                List<File> files = new LinkedList<>();
                selectedEntries.forEach(entryWrapper -> files.add(entryWrapper.getEntry().getFilesystemEntry()));
                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(row.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.putFiles(files);
                db.setContent(cc);
                event.consume();
            }
        });
        row.setOnDragEntered(event -> {
            if (!row.isEmpty()) {
                if (row.getItem().getEntry().getEntryProperties().isDirectory()) {
                    super.getFocusModel().focus(row.getIndex());
                }
            }
            event.consume();
        });
        row.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                files.forEach(fileToMove -> {
                    AbstractEntry fileToMoveEntry = fileToMove.isFile()
                            ? new FileEntry(fileToMove.getAbsolutePath())
                            : new DirectoryEntry(fileToMove.getAbsolutePath());
                    if (!row.isEmpty()) {
                        DirectoryEntry targetEntry;
                        if (row.getItem().getEntry().getEntryProperties().isDirectory()) {
                            File target = row.getItem().getEntry().getFilesystemEntry();
                            targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        } else {
                            File target = new File(this.getRootPath());
                            targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        }
                        if (!fileToMoveEntry.existsInTargetDirectory(targetEntry)) {
                            fileToMoveEntry.move(targetEntry);
                        }
                        event.consume();
                        MainWindow.getLeftDirectoryView().reload();
                        MainWindow.getRightDirectoryView().reload();
                    } else {
                        File target = new File(this.getRootPath());
                        DirectoryEntry targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        fileToMoveEntry.move(targetEntry);
                        event.consume();
                        MainWindow.getLeftDirectoryView().reload();
                        MainWindow.getRightDirectoryView().reload();
                    }
                });
            }
        });
        row.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        return row;
    }

    public void reload() {
        changePath(rootPath);
    }

}
