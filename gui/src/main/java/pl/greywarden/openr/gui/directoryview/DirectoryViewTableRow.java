package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.TableRow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;
import pl.greywarden.openr.gui.directoryview.context_menu.MultipleSelectionContextMenu;
import pl.greywarden.openr.gui.directoryview.context_menu.NoSelectionContextMenu;
import pl.greywarden.openr.gui.directoryview.context_menu.SingleSelectionContextMenu;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Log4j
public class DirectoryViewTableRow extends TableRow<EntryWrapper> {

    private final DirectoryView parent;

    public DirectoryViewTableRow(DirectoryView parent) {
        super();
        this.parent = parent;
        setMouseClickHandler();
        setDragHandler();
    }

    private void setMouseClickHandler() {
        super.setOnMouseClicked(event -> {
            if (MouseButton.PRIMARY.equals(event.getButton())) {
                if (event.getClickCount() == 2 && (!super.isEmpty())) {
                    handleDoubleClick();
                }
            } else if (MouseButton.SECONDARY.equals(event.getButton())) {
                handleRightClick(event);
            }
        });
    }

    private void handleDoubleClick() {
        EntryWrapper rowData = super.getItem();
        if (rowData.getEntry().getEntryProperties().isDirectory()) {
            parent.getSelectionModel().setSelectionMode(null);
            parent.changePath(rowData.getEntry().getEntryProperties().getAbsolutePath());
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

    private void handleRightClick(MouseEvent event) {
        if (super.isEmpty()) {
            new NoSelectionContextMenu(parent).show(this, event.getScreenX(), event.getScreenY());
        } else {
                EntryWrapper rowData = super.getItem();
                AbstractEntry target = rowData.getEntry();
            if (parent.getSelectionModel().getSelectedItems().size() == 1) {
                if (!(target instanceof ParentDirectoryEntry)) {
                    new SingleSelectionContextMenu(parent, target)
                            .show(this, event.getScreenX(), event.getScreenY());
                }
            } else {
                if (!(target instanceof ParentDirectoryEntry)) {
                    new MultipleSelectionContextMenu(parent)
                            .show(this, event.getScreenX(), event.getScreenY());
                }
            }
        }
    }

    private void setDragHandler() {
        super.setOnDragDetected(event -> {
            if (!super.isEmpty()) {
                List<EntryWrapper> selectedEntries = parent.getSelectionModel().getSelectedItems();
                List<File> files = new LinkedList<>();
                selectedEntries.forEach(entryWrapper -> files.add(entryWrapper.getEntry().getFilesystemEntry()));
                Dragboard db = super.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(super.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.putFiles(files);
                db.setContent(cc);
                event.consume();
            }
        });
        super.setOnDragEntered(event -> {
            if (!super.isEmpty()) {
                if (super.getItem().getEntry().getEntryProperties().isDirectory()) {
                    parent.getFocusModel().focus(super.getIndex());
                }
            }
            event.consume();
        });
        super.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                files.forEach(fileToMove -> {
                    AbstractEntry fileToMoveEntry = fileToMove.isFile()
                            ? new FileEntry(fileToMove.getAbsolutePath())
                            : new DirectoryEntry(fileToMove.getAbsolutePath());
                    if (!super.isEmpty()) {
                        DirectoryEntry targetEntry;
                        if (super.getItem().getEntry().getEntryProperties().isDirectory()) {
                            File target = super.getItem().getEntry().getFilesystemEntry();
                            targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        } else {
                            File target = new File(parent.getRootPath());
                            targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        }
                        if (!fileToMoveEntry.existsInTargetDirectory(targetEntry)) {
                            fileToMoveEntry.move(targetEntry);
                        }
                        event.consume();
                        MainWindow.getLeftDirectoryView().reload();
                        MainWindow.getRightDirectoryView().reload();
                    } else {
                        File target = new File(parent.getRootPath());
                        DirectoryEntry targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        fileToMoveEntry.move(targetEntry);
                        event.consume();
                        MainWindow.getLeftDirectoryView().reload();
                        MainWindow.getRightDirectoryView().reload();
                    }
                });
            }
        });
        super.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }
}
