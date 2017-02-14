package pl.greywarden.openr.gui.menu.file;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.greywarden.openr.gui.dialogs.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.templates.Template;

import java.util.LinkedList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class NewDocumentMenu extends Menu {

    private final List<MenuItem> items = new LinkedList<>();

    public NewDocumentMenu() {
        super.setText(getString("new-document-menu"));
        Template.getTemplatesByType("document").forEach(this::createDocumentItem);
        super.getItems().addAll(items);
    }

    private void createDocumentItem(Template template) {
        MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
        item.setOnAction(event ->
                new CreateFileDialog(template));
        items.add(item);
    }

    public NewDocumentMenu(DirectoryView selectedView) {
        super.setText(getString("new-document-menu"));
        Template.getTemplatesByType("document")
                .forEach(template -> createDocumentItem(selectedView, template));
        super.getItems().addAll(items);
    }

    private void createDocumentItem(DirectoryView selectedView, Template template) {
        MenuItem item = new MenuItem(getString(template.getName() + "-menu-item"));
        item.setOnAction(event ->
                new CreateFileDialog(template, selectedView));
        items.add(item);
    }

}
