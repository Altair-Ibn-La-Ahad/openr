package pl.greywarden.openr.gui.directoryview.context_menu;

import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.gui.dialogs.ConfirmDeleteDialog;
import pl.greywarden.openr.gui.dialogs.EntryInfoDialog;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.dialogs.PreviewImageDialog;
import pl.greywarden.openr.gui.dialogs.RenameDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.property_editor.PropertyEditorDialog;
import pl.greywarden.openr.gui.menu.NewDocumentMenu;
import pl.greywarden.openr.gui.menu.NewFileMenu;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class SingleSelectionContextMenu extends ContextMenu {

    private final AbstractEntry entry;
    private final DirectoryView directoryView;

    private static final Set<String> editableFiles;

    static {
        editableFiles = new HashSet<>();
        editableFiles.add("properties");
    }

    public SingleSelectionContextMenu(DirectoryView directoryView, AbstractEntry entry) {
        this.entry = entry;
        this.directoryView = directoryView;
        buildOptions();
    }

    private void buildOptions() {
        MenuItem open = createOpenMenuItem();
        MenuItem preview = createPreviewMenuItem();
        MenuItem rename = createRenameMenuItem();
        MenuItem cut = createCutMenuItem();
        MenuItem copy = createCopyMenuItem();
        MenuItem paste = createPasteMenuItem();
        MenuItem createDirectory = createCreateDirectoryMenuItem();
        MenuItem moveToTrash = createMoveToTrashMenuItem();
        MenuItem deletePermanently = createDeletePermanentlyMenuItem();
        MenuItem properties = createPropertiesMenuItem();
        MenuItem edit = createEditMenuItem();

        Menu newFile = new NewFileMenu(directoryView);
        Menu newDocument = new NewDocumentMenu();

        super.getItems().addAll(
                open, edit, preview, rename,
                new SeparatorMenuItem(),
                newFile, newDocument, createDirectory,
                new SeparatorMenuItem(),
                cut, copy, paste,
                new SeparatorMenuItem(),
                moveToTrash,
                deletePermanently,
                new SeparatorMenuItem(),
                properties);

    }

    private MenuItem createEditMenuItem() {
        MenuItem edit = new MenuItem(getString("edit"));
        String fileExtension = FilenameUtils.getExtension(getSelectedFile().getName());
        edit.setVisible(editableFiles.contains(fileExtension));
        edit.setOnAction(event -> {
            if ("properties".equalsIgnoreCase(fileExtension)) {
                new PropertyEditorDialog(getSelectedFile());
            }
        });
        return edit;
    }

    private MenuItem createPropertiesMenuItem() {
        MenuItem properties = new MenuItem(getString("properties"));
        properties.setOnAction(event -> new EntryInfoDialog(entry.getFilesystemEntry()).show());
        return properties;
    }

    private MenuItem createDeletePermanentlyMenuItem() {
        MenuItem deletePermanently = new MenuItem(getString("delete-permanently-menu-item"));
        deletePermanently.setGraphic(IconManager.getIcon("delete-permanent-small"));
        deletePermanently.setOnAction(event -> new ConfirmDeleteDialog(directoryView));
        return deletePermanently;
    }

    private MenuItem createMoveToTrashMenuItem() {
        MenuItem moveToTrash = new MenuItem(getString("move-to-trash"));
        moveToTrash.setGraphic(IconManager.getIcon("trash"));
        moveToTrash.setOnAction(event -> handleMoveToTrashAction());
        return moveToTrash;
    }

    private void handleMoveToTrashAction() {
        entry.moveToTrash();
        directoryView.reload();
    }

    private MenuItem createCreateDirectoryMenuItem() {
        MenuItem createDirectory = new MenuItem(getString("create-directory"));
        createDirectory.setOnAction(event -> new NewDirectoryDialog(directoryView));
        return createDirectory;
    }

    private MenuItem createPasteMenuItem() {
        MenuItem paste = new MenuItem(getString("paste"));
        paste.setGraphic(IconManager.getIcon("paste"));
        paste.setOnAction(event -> handlePasteAction());
        paste.disableProperty().bind(AbstractEntry.clipboardEmptyBinding());
        return paste;
    }

    private void handlePasteAction() {
        EntryWrapper selectedItem = directoryView.getSelectionModel().getSelectedItem();
        AbstractEntry target = selectedItem.getEntry();
        DirectoryEntry root = new DirectoryEntry(directoryView.getRootPath());
        File targetFile = target.getFilesystemEntry();
        entry.paste(targetFile.isDirectory() ? target : root);
        directoryView.reload();
        if (clearClipboard()) {
            AbstractEntry.clearClipboard();
        }
    }

    private boolean clearClipboard() {
        return !Boolean.valueOf(ConfigManager.getSetting(Setting.KEEP_CLIPBOARD.CODE));
    }

    private MenuItem createCopyMenuItem() {
        MenuItem copy = new MenuItem(getString("copy"));
        copy.setGraphic(IconManager.getIcon("copy"));
        copy.setOnAction(event -> entry.copy());
        return copy;
    }

    private MenuItem createCutMenuItem() {
        MenuItem cut = new MenuItem(getString("cut"));
        cut.setGraphic(IconManager.getIcon("cut"));
        cut.setOnAction(event -> entry.cut());
        return cut;
    }

    private MenuItem createRenameMenuItem() {
        MenuItem rename = new MenuItem(getString("rename"));
        rename.setOnAction(event -> new RenameDialog(directoryView));
        return rename;
    }

    private MenuItem createPreviewMenuItem() {
        MenuItem preview = new MenuItem(getString("preview"));
        preview.setVisible(isImage());
        preview.setOnAction(event -> showPreviewImageDialog());
        return preview;
    }

    private void showPreviewImageDialog() {
        Platform.runLater(() -> new PreviewImageDialog(getSelectedFile()).show());
    }

    private MenuItem createOpenMenuItem() {
        MenuItem open = new MenuItem(getString("open"));
        String FONT_BOLD = "-fx-font-weight: bold";
        open.setStyle(FONT_BOLD);
        open.setOnAction(event -> handleOpenEvent());
        return open;
    }

    private void handleOpenEvent() {
        File selectedFile = getSelectedFile();
        if (Desktop.isDesktopSupported() && selectedFile.isFile()) {
            browseSelectedFile(selectedFile);
        } else {
            changePathToSelected();
        }
    }

    private File getSelectedFile() {
        return entry.getFilesystemEntry();
    }

    private void changePathToSelected() {
        directoryView.changePath(entry.getEntryProperties().getAbsolutePath());
    }

    private void browseSelectedFile(File target) {
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(target.toURI());
            } catch (IOException exception) {
                log.error("Unable to open selected file", exception);
            }
        }).start();
    }

    private boolean isImage() {
        try {
            String contentType = Files.probeContentType(getSelectedFile().toPath());
            return contentType.contains("image");
        } catch (IOException ignored) {
            return false;
        }
    }
}
