package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.gui.scenes.NewDocumentMenu;
import pl.greywarden.openr.gui.scenes.NewFileMenu;
import pl.greywarden.openr.i18n.I18nManager;

public class CreateNewEntryContextMenu extends ContextMenu {

    private final I18nManager i18n = I18nManager.getInstance();

    private final DirectoryView view;

    public CreateNewEntryContextMenu(DirectoryView view) {
        super();
        i18n.setBundle("create-new-entry");
        this.view = view;
        buildOptions();
    }

    private void buildOptions() {
        Menu newFile = new NewFileMenu(view, null);
        Menu newDocument = new NewDocumentMenu(view, null);
        MenuItem newDirectory = new MenuItem(i18n.getString("create-directory"));
        newDirectory.setOnAction(event -> new NewDirectoryDialog(view));
        super.getItems().setAll(newFile, newDocument, newDirectory);
    }

}
