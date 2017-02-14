package pl.greywarden.openr.gui.menu.file;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.util.LinkedList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class NewFileMenu extends Menu {

    private final List<MenuItem> items = new LinkedList<>();

    public NewFileMenu() {
        super.setText(getString("new-file-menu"));
        Template.getTemplatesByType("text").forEach(this::createTextMenuItem);
        super.getItems().addAll(items);
    }

    private void createTextMenuItem(Template template) {
        MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
        item.setOnAction(event ->
                new CreateFileDialog(template));
        items.add(item);
    }

    public NewFileMenu(DirectoryView selectedView) {
        super.setText(getString("new-file-menu"));
        Template.getTemplatesByType("text")
                .forEach(template -> createTextMenuItem(selectedView, template));
        super.getItems().addAll(items);
    }

    private void createTextMenuItem(DirectoryView selectedView, Template template) {
        MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
        item.setOnAction(event ->
                new CreateFileDialog(template, selectedView));
        items.add(item);
    }

}
