package pl.greywarden.openr.gui.scenes;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.CreateFileDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

import static pl.greywarden.openr.i18n.I18nManager.getString;

public class NewDocumentMenu extends Menu {

    public NewDocumentMenu(DirectoryView left, DirectoryView right) {
        super.setText(getString("new-document-menu"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().stream().filter(template -> "document".equals(template.getType()))
                .forEach(template -> {
                    MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
                    item.setOnAction(event ->
                            new CreateFileDialog(template,
                                    left, right));
                    items.add(item);
                });
        super.getItems().addAll(items);
    }

}
