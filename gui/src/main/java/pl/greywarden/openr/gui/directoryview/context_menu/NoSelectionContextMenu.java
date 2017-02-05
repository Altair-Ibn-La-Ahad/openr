package pl.greywarden.openr.gui.directoryview.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.main_window.menu.NewDocumentMenu;
import pl.greywarden.openr.gui.scenes.main_window.menu.NewFileMenu;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class NoSelectionContextMenu extends ContextMenu {

    private final DirectoryView view;

    public NoSelectionContextMenu(DirectoryView view) {
        super();
        this.view = view;
        buildOptions();
    }

    private void buildOptions() {
        Menu newFile = new NewFileMenu(view);
        Menu newDocument = new NewDocumentMenu(view);
        MenuItem newDirectory = new MenuItem(getString("create-directory-menu-item"));
        newDirectory.setOnAction(event -> new NewDirectoryDialog(view));
        MenuItem paste = new MenuItem(getString("paste"));
        paste.setGraphic(IconManager.getIcon("paste"));
        final DirectoryEntry target = new DirectoryEntry(view.getRootPath());
        paste.setOnAction(event -> {
            target.paste(target);
            view.reload();
            if (!Boolean.valueOf(ConfigManager.getSetting(Setting.KEEP_CLIPBOARD.CODE))) {
                AbstractEntry.clearClipboard();
            }
        });
        paste.disableProperty().bind(AbstractEntry.clipboardEmptyBinding());
        super.getItems().setAll(
                newFile, newDocument, newDirectory,
                new SeparatorMenuItem(),
                paste);
    }

}
