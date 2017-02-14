package pl.greywarden.openr.gui.directoryview.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.menu.file.NewDocumentMenu;
import pl.greywarden.openr.gui.menu.file.NewFileMenu;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class NoSelectionContextMenu extends ContextMenu {

    private final DirectoryView view;

    public NoSelectionContextMenu(DirectoryView view) {
        super();
        this.view = view;
        buildOptions();
    }

    private void buildOptions() {
        MenuItem newDirectory = createNewDirectoryMenuItem();
        MenuItem paste = createPasteMenuItem();

        Menu newFile = new NewFileMenu(view);
        Menu newDocument = new NewDocumentMenu(view);
        super.getItems().setAll(
                newFile, newDocument, newDirectory,
                new SeparatorMenuItem(),
                paste);
    }

    private MenuItem createNewDirectoryMenuItem() {
        MenuItem newDirectory = new MenuItem(getString("create-directory-menu-item"));
        newDirectory.setOnAction(event -> new NewDirectoryDialog(view));
        return newDirectory;
    }

    private MenuItem createPasteMenuItem() {
        MenuItem paste = new MenuItem(getString("paste"));
        paste.setGraphic(IconManager.getProgramIcon("paste"));
        paste.setOnAction(event -> handlePasteAction());
        paste.disableProperty().bind(AbstractEntry.clipboardEmptyBinding());
        return paste;
    }

    private void handlePasteAction() {
        DirectoryEntry target = new DirectoryEntry(view.getRootPath());
        target.paste(target);
        view.reload();
        if (cleanClipboard()) {
            AbstractEntry.clearClipboard();
        }
    }

    private boolean cleanClipboard() {
        return !Boolean.valueOf(getSetting(Setting.KEEP_CLIPBOARD));
    }

}
