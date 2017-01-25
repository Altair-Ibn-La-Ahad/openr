package pl.greywarden.openr.gui.scenes;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

public class NewFileMenu extends Menu {

    public NewFileMenu(DirectoryView left, DirectoryView right) {
        I18nManager i18n = I18nManager.getInstance();
        String bundle = i18n.getBundle();
        i18n.setBundle("new-file-menu");
        super.setText(i18n.getString("new-file"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().stream().filter(template -> "text".equals(template.getType()))
                .forEach(template -> {
                    MenuItem item = new MenuItem(i18n.getString(template.getName()));
                    item.setOnAction(event ->
                            new CreateFileDialog(template,
                                    left, right));
                    items.add(item);
                });
        super.getItems().addAll(items);
        i18n.setBundle(bundle);
    }

}
