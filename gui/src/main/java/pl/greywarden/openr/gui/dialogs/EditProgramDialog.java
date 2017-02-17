package pl.greywarden.openr.gui.dialogs;

import pl.greywarden.openr.gui.favourite_programs.ProgramWrapper;
import pl.greywarden.openr.gui.menu.favourite_programs.FavouriteProgramsMenu;

public class EditProgramDialog extends AddNewProgramDialog {

    private final ProgramWrapper selected;
    private final FavouriteProgramsMenu parent;

    public EditProgramDialog(FavouriteProgramsMenu parent, ProgramWrapper selected) {
        super(parent);
        this.selected = selected;
        this.parent = parent;
        getNameInput().setText(selected.getName());
        getPathInput().setText(selected.getPath());
        getIconInput().setText(selected.getIcon());
        this.getNameInput().requestFocus();
    }

    @Override
    protected void handleConfirm() {
        selected.setName(getNameInput().getText());
        selected.setIcon(getIconInput().getText());
        selected.setPath(getPathInput().getText());
        parent.invalidate();
    }
}
