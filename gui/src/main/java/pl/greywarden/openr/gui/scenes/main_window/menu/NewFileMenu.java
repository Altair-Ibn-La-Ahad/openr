package pl.greywarden.openr.gui.scenes.main_window.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.util.ArrayList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class NewFileMenu extends Menu {

    public NewFileMenu() {
        super.setText(getString("new-file-menu"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().stream().filter(template -> "text".equals(template.getType()))
                .forEach(template -> {
                    MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
                    item.setOnAction(event ->
                            new CreateFileDialog(template).showDialog());
                    items.add(item);
                });
        super.getItems().addAll(items);
    }

    public NewFileMenu(DirectoryView selectedView) {
        super.setText(getString("new-file-menu"));
        List<MenuItem> items = new ArrayList<>();
        Template.getAvailableTemplates().stream().filter(template -> "text".equals(template.getType()))
                .forEach(template -> {
                    MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
                    item.setOnAction(event ->
                            new CreateFileDialog(template, selectedView).showDialog());
                    items.add(item);
                });
        super.getItems().addAll(items);
    }

}
