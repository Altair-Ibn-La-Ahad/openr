package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.gui.dialogs.NewDirectoryDialog;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

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
        Menu newFile = createNewFileMenu();
        MenuItem newDirectory = new MenuItem(i18n.getString("create-directory"));
        newDirectory.setOnAction(event -> new NewDirectoryDialog(view));
        super.getItems().setAll(newFile, newDirectory);
    }

    private Menu createNewFileMenu() {
        Menu menu = new Menu(i18n.getString("new-file"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().forEach(template -> {
            MenuItem item = new MenuItem(i18n.getString(template.getName()));
            item.setOnAction(event ->
                    new CreateFileDialog(template,
                            view, null));
            items.add(item);
        });
        menu.getItems().addAll(items);
        return menu;
    }

}
