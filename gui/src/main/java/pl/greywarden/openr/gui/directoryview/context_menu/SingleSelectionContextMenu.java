package pl.greywarden.openr.gui.directoryview.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.dialogs.ConfirmDeleteDialog;
import pl.greywarden.openr.gui.dialogs.EntryInfoDialog;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.dialogs.RenameDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.main_window.menu.NewDocumentMenu;
import pl.greywarden.openr.gui.scenes.main_window.menu.NewFileMenu;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class SingleSelectionContextMenu extends ContextMenu {

    private final AbstractEntry entry;
    private final DirectoryView directoryView;

    public SingleSelectionContextMenu(DirectoryView directoryView, AbstractEntry entry) {
        this.entry = entry;
        this.directoryView = directoryView;
        buildOptions();
    }

    private void buildOptions() {
        MenuItem open = new MenuItem(getString("open"));
        open.setStyle("-fx-font-weight: bold");
        open.setOnAction(event -> {
            File target = new File(entry.getEntryProperties().getAbsolutePath());
            if (Desktop.isDesktopSupported() && target.isFile()) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().browse(target.toURI());
                    } catch (IOException exception) {
                        log.error("Unable to open selected file", exception);
                    }
                }).start();
            } else {
                directoryView.changePath(entry.getEntryProperties().getAbsolutePath());
            }
        });

        MenuItem rename = new MenuItem(getString("rename"));
        rename.setOnAction(event -> new RenameDialog(directoryView));

        Menu newFile = new NewFileMenu(directoryView);
        Menu newDocument = new NewDocumentMenu();

        MenuItem createDirectory = new MenuItem(getString("create-directory"));
        createDirectory.setOnAction(event -> new NewDirectoryDialog(directoryView));

        MenuItem cut = new MenuItem(getString("cut"));
        MenuItem copy = new MenuItem(getString("copy"));
        MenuItem paste = new MenuItem(getString("paste"));

        cut.setGraphic(IconManager.getIcon("cut"));
        cut.setOnAction(event -> entry.cut());

        copy.setGraphic(IconManager.getIcon("copy"));
        copy.setOnAction(event -> entry.copy());

        paste.setGraphic(IconManager.getIcon("paste"));
        paste.setOnAction(event -> {
            AbstractEntry target = directoryView.getSelectionModel().getSelectedItem().getEntry();
            if (target.getEntryProperties().isDirectory()) {
                entry.paste(target);
            } else {
                DirectoryEntry root = new DirectoryEntry(directoryView.getRootPath());
                entry.paste(root);
            }
            directoryView.reload();
            if (!Boolean.valueOf(ConfigManager.getSetting(Setting.KEEP_CLIPBOARD.CODE))) {
                AbstractEntry.clearClipboard();
            }
        });

        paste.disableProperty().bind(AbstractEntry.clipboardEmptyBinding());

        MenuItem moveToTrash = new MenuItem(getString("move-to-trash"));
        moveToTrash.setGraphic(IconManager.getIcon("trash"));
        moveToTrash.setOnAction(event -> {
            entry.moveToTrash();
            directoryView.reload();
        });

        MenuItem deletePermanently = new MenuItem(getString("delete-permanently-menu-item"));
        deletePermanently.setGraphic(IconManager.getIcon("delete-permanent-small"));
        deletePermanently.setOnAction(event -> new ConfirmDeleteDialog(directoryView));

        MenuItem properties = new MenuItem(getString("properties"));
        properties.setOnAction(event -> new EntryInfoDialog(entry.getEntryProperties().getAbsolutePath()).show());

        super.getItems().addAll(
                open, rename,
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

}
