package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.scenes.main_window.menu.NewDocumentMenu;
import pl.greywarden.openr.gui.scenes.main_window.menu.NewFileMenu;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class CreateNewEntryContextMenu extends ContextMenu {

    private final DirectoryView view;

    public CreateNewEntryContextMenu(DirectoryView view) {
        super();
        this.view = view;
        buildOptions();
    }

    private void buildOptions() {
        Menu newFile = new NewFileMenu(view);
        Menu newDocument = new NewDocumentMenu(view);
        MenuItem newDirectory = new MenuItem(getString("create-directory-menu-item"));
        newDirectory.setOnAction(event -> new NewDirectoryDialog(view));
        super.getItems().setAll(newFile, newDocument, newDirectory);
    }

}
