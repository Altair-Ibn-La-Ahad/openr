package pl.greywarden.openr.gui.directoryview.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;
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
import pl.greywarden.openr.gui.dialogs.property_editor.PropertyEditorDialog;
import pl.greywarden.openr.gui.menu.file.NewDocumentMenu;
import pl.greywarden.openr.gui.menu.file.NewFileMenu;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

@Log4j
public class SingleSelectionContextMenu extends ContextMenu {

    private final AbstractEntry entry;
    private final DirectoryView directoryView;

    private static final Set<String> editableFiles;

    private static final Map<String, Class> supportedFilePreview;

    static {
        editableFiles = new HashSet<>();
        editableFiles.add("properties");

        supportedFilePreview = new HashMap<>();
        supportedFilePreview.put("image", PreviewImageDialog.class);
        supportedFilePreview.put("text", PreviewTextDialog.class);
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
        deletePermanently.setGraphic(IconManager.getProgramIcon("delete-permanent-small"));
        deletePermanently.setOnAction(event -> new ConfirmDeleteDialog(directoryView));
        return deletePermanently;
    }

    private MenuItem createMoveToTrashMenuItem() {
        MenuItem moveToTrash = new MenuItem(getString("move-to-trash"));
        moveToTrash.setGraphic(IconManager.getProgramIcon("trash"));
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
        paste.setGraphic(IconManager.getProgramIcon("paste"));
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
        return !Boolean.valueOf(getSetting(Setting.KEEP_CLIPBOARD));
    }

    private MenuItem createCopyMenuItem() {
        MenuItem copy = new MenuItem(getString("copy"));
        copy.setGraphic(IconManager.getProgramIcon("copy"));
        copy.setOnAction(event -> entry.copy());
        return copy;
    }

    private MenuItem createCutMenuItem() {
        MenuItem cut = new MenuItem(getString("cut"));
        cut.setGraphic(IconManager.getProgramIcon("cut"));
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
        preview.setVisible(isPreviewSupported());
        preview.setOnAction(event -> showPreviewDialog());
        return preview;
    }

    @SuppressWarnings("unchecked")
    private void showPreviewDialog() {
        try {
            supportedFilePreview.get(getSimpleFileType())
                    .getDeclaredConstructor(File.class).newInstance(getSelectedFile());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String getSimpleFileType() {
        try {
            return Files.probeContentType(getSelectedFile().toPath()).split("/")[0];
        } catch (IOException | NullPointerException e) {
            return "";
        }
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

    private boolean isPreviewSupported() {
        try {
            String contentType = Files.probeContentType(getSelectedFile().toPath());
            for (String type : supportedFilePreview.keySet()) {
                if (contentType.contains(type)) {
                    return true;
                }
            }
            return false;
        } catch (IOException ignored) {
            return false;
        }
    }
}
