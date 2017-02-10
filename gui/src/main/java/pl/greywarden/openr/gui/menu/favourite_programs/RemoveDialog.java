package pl.greywarden.openr.gui.menu.favourite_programs;

import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.favourite_programs.Program;

public class RemoveDialog extends Dialog<Boolean> {

    private final FavouriteProgramsMenu parent;

    public RemoveDialog(FavouriteProgramsMenu parent) {
        super();
        super.initStyle(StageStyle.UTILITY);

        this.parent = parent;

        ListView<Program> programs = createProgramsListView();

        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK);
        super.getDialogPane().setContent(programs);
        super.showAndWait();
    }

    private ListView<Program> createProgramsListView() {
        ListView<Program> programs = new ListView<>();
        programs.getItems().setAll(FavouritePrograms.getPrograms());
        programs.setCellFactory(param -> createProgramListCell());
        programs.setOnKeyPressed(event -> handleKeyPressed(programs, event));
        return programs;
    }

    private void handleKeyPressed(ListView<Program> programsList, KeyEvent event) {
        if (KeyCode.DELETE.equals(event.getCode())) {
            removeSelectedItem(programsList);
        }
    }

    private void removeSelectedItem(ListView<Program> programs) {
        Program selectedItem = programs.getSelectionModel().getSelectedItem();
        programs.getItems().remove(selectedItem);
        FavouritePrograms.remove(selectedItem);
        parent.invalidate();
    }

    private ListCell<Program> createProgramListCell() {
        return new ListCell<Program>() {
            @Override
            protected void updateItem(Program item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
    }
}
