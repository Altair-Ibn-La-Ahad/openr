package pl.greywarden.openr.gui.scenes.main_window.menu.favourite_programs;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;

import java.util.LinkedList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class FavouriteProgramsMenu extends Menu {

    public FavouriteProgramsMenu() {
        super(getString("programs"));
        invalidate();
    }

    public void invalidate() {
        super.getItems().clear();
        createMenu();
    }

    private void createMenu() {
        List<MenuItem> items = new LinkedList<>();
        FavouritePrograms.getPrograms().forEach(program -> items.add(new ProgramMenuItem(program)));
        items.add(new SeparatorMenuItem());

        MenuItem addProgram = new MenuItem(getString("add-new-program"));
        addProgram.setGraphic(IconManager.getIcon("plus"));
        addProgram.setOnAction(event -> new AddNewDialog(this));

        MenuItem remove = new MenuItem(getString("remove-program"));
        remove.setGraphic(IconManager.getIcon("delete-permanent-small"));
        remove.setOnAction(event -> new RemoveDialog(this));

        items.add(addProgram);
        items.add(remove);

        super.getItems().setAll(items);
    }
}
