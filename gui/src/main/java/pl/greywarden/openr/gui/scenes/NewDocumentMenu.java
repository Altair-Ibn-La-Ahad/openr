package pl.greywarden.openr.gui.scenes;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

public class NewDocumentMenu extends Menu {

    public NewDocumentMenu(DirectoryView left, DirectoryView right) {
        I18nManager i18n = I18nManager.getInstance();
        String bundle = i18n.getBundle();
        i18n.setBundle("new-document-menu");
        super.setText(i18n.getString("new-document"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().stream().filter(template -> "document".equals(template.getType()))
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
