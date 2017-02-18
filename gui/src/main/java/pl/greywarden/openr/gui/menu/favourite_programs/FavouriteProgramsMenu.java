package pl.greywarden.openr.gui.menu.favourite_programs;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.dialogs.AddNewProgramDialog;
import pl.greywarden.openr.gui.dialogs.ModifyProgramsDialog;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.favourite_programs.ProgramWrapper;

import java.util.LinkedList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class FavouriteProgramsMenu extends Menu {

    private List<MenuItem> items;

    public FavouriteProgramsMenu() {
        super(getString("programs"));
        invalidate();
    }

    public void invalidate() {
        super.getItems().clear();
        createMenu();
    }

    private void createMenu() {
        List<MenuItem> items = createProgramItems();
        MenuItem addProgram = createAddProgramMenuItem();
        MenuItem edit = createEditMenuItem();

        items.add(addProgram);
        items.add(edit);

        super.getItems().setAll(items);
    }

    private List<MenuItem> createProgramItems() {
        items = new LinkedList<>();
        FavouritePrograms.getPrograms().forEach(this::addToItems);
        items.add(new SeparatorMenuItem());
        return items;
    }

    private boolean addToItems(ProgramWrapper program) {
        return items.add(new ProgramMenuItem(program));
    }

    private MenuItem createAddProgramMenuItem() {
        MenuItem addProgram = new MenuItem(getString("add-new-program"));
        addProgram.setGraphic(IconManager.getProgramIcon("plus"));
        addProgram.setOnAction(event -> new AddNewProgramDialog(this).showDialog());
        return addProgram;
    }

    private MenuItem createEditMenuItem() {
        MenuItem remove = new MenuItem(getString("edit-program"));
        remove.setGraphic(IconManager.getProgramIcon("edit"));
        remove.setOnAction(event -> new ModifyProgramsDialog(this));
        return remove;
    }
}
