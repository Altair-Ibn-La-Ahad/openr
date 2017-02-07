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
        if (isDirectory(rowData)) {
            parent.getSelectionModel().setSelectionMode(null);
            parent.changePath(rowData.getEntry().getEntryProperties().getAbsolutePath());
        } else {
            browseSelectedFile(rowData);
        }
    }

    private boolean isDirectory(EntryWrapper rowData) {
        return rowData.getEntry().getEntryProperties().isDirectory();
    }

    private void browseSelectedFile(EntryWrapper rowData) {
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

    private void handleRightClick(MouseEvent event) {
        if (super.isEmpty()) {
            new NoSelectionContextMenu(parent).show(this, event.getScreenX(), event.getScreenY());
        } else {
            EntryWrapper rowData = super.getItem();
            AbstractEntry target = rowData.getEntry();
            if (isSelectionSingle()) {
                if (notParentDirectory(target)) {
                    new SingleSelectionContextMenu(parent, target)
                            .show(this, event.getScreenX(), event.getScreenY());
                }
            } else {
                if (notParentDirectory(target)) {
                    new MultipleSelectionContextMenu(parent)
                            .show(this, event.getScreenX(), event.getScreenY());
                }
            }
        }
    }

    private boolean isSelectionSingle() {
        return parent.getSelectionModel().getSelectedItems().size() == 1;
    }

    private boolean notParentDirectory(AbstractEntry target) {
        return !(target instanceof ParentDirectoryEntry);
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
            if (!super.isEmpty() && isDirectory()) {
                focusHoveredItem();
            }
            event.consume();
        });
        super.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                files.forEach(fileToMove -> {
                    AbstractEntry fileToMoveEntry = getFileToMoveEntry(fileToMove);
                    if (!super.isEmpty()) {
                        DirectoryEntry targetEntry;
                        if (isDirectory()) {
                            File target = super.getItem().getEntry().getFilesystemEntry();
                            targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        } else {
                            File target = new File(parent.getRootPath());
                            targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        }
                        if (!fileToMoveEntry.existsInTargetDirectory(targetEntry)) {
                            fileToMoveEntry.move(targetEntry);
                        }
                    } else {
                        File target = new File(parent.getRootPath());
                        DirectoryEntry targetEntry = new DirectoryEntry(target.getAbsolutePath());
                        fileToMoveEntry.move(targetEntry);
                    }
                    event.consume();
                    reloadVisibleDirectoryViews();
                });
            }
        });
        super.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private void focusHoveredItem() {
        parent.getFocusModel().focus(super.getIndex());
    }

    private boolean isDirectory() {
        return isDirectory(super.getItem());
    }

    private void reloadVisibleDirectoryViews() {
        if (MainWindow.getLeftWrapper().isVisible()) {
            MainWindow.getLeftDirectoryView().reload();
        }
        if (MainWindow.getRightWrapper().isVisible()) {
            MainWindow.getRightDirectoryView().reload();
        }
    }

    private AbstractEntry getFileToMoveEntry(File fileToMove) {
        return fileToMove.isFile()
                ? new FileEntry(fileToMove.getAbsolutePath())
                : new DirectoryEntry(fileToMove.getAbsolutePath());
    }
}
