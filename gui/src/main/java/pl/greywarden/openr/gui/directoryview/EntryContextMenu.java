package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.gui.IconManager;
import pl.greywarden.openr.gui.dialogs.ConfirmDeleteDialog;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.gui.dialogs.EntryInfoDialog;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.dialogs.RenameDialog;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class EntryContextMenu extends ContextMenu {

    private final I18nManager i18n = I18nManager.getInstance();
    private final AbstractEntry entry;
    private final DirectoryView directoryView;

    public EntryContextMenu(DirectoryView directoryView, AbstractEntry entry) {
        this.entry = entry;
        this.directoryView = directoryView;
        i18n.setBundle("entry-context-menu");
        buildOptions();
    }

    private void buildOptions() {
        MenuItem open = new MenuItem(i18n.getString("open"));
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

        MenuItem rename = new MenuItem(i18n.getString("rename"));
        rename.setOnAction(event -> new RenameDialog(directoryView));

        Menu newFile = createNewFileMenu();

        MenuItem createDirectory = new MenuItem(i18n.getString("create-directory"));
        createDirectory.setOnAction(event -> new NewDirectoryDialog(directoryView));

        MenuItem cut = new MenuItem(i18n.getString("cut"));
        MenuItem copy = new MenuItem(i18n.getString("copy"));
        MenuItem paste = new MenuItem(i18n.getString("paste"));

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
        });

        paste.disableProperty().bind(AbstractEntry.clipboardEmptyBinding());

        MenuItem moveToTrash = new MenuItem(i18n.getString("move-to-trash"));
        moveToTrash.setGraphic(IconManager.getIcon("trash"));
        moveToTrash.setOnAction(event -> {
            entry.moveToTrash();
            directoryView.reload();
        });

        MenuItem deletePermanently = new MenuItem(i18n.getString("delete-permanently"));
        deletePermanently.setGraphic(IconManager.getIcon("delete-permanent-small"));
        deletePermanently.setOnAction(event -> new ConfirmDeleteDialog(directoryView));

        MenuItem properties = new MenuItem(i18n.getString("properties"));
        properties.setOnAction(event -> new EntryInfoDialog(entry.getEntryProperties().getAbsolutePath()).show());

        super.getItems().addAll(
                open, rename,
                new SeparatorMenuItem(),
                newFile, createDirectory,
                new SeparatorMenuItem(),
                cut, copy, paste,
                new SeparatorMenuItem(),
                moveToTrash,
                deletePermanently,
                new SeparatorMenuItem(),
                properties);

    }

    private Menu createNewFileMenu() {
        Menu menu = new Menu(i18n.getString("new-file"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().forEach(template -> {
            MenuItem item = new MenuItem(i18n.getString(template.getName()));
            item.setOnAction(event ->
                    new CreateFileDialog(template,
                            directoryView, null));
            items.add(item);
        });
        menu.getItems().addAll(items);
        return menu;
    }

}
