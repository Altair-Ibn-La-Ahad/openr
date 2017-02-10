package pl.greywarden.openr.gui.scenes.main_window.menu.favourite_programs;

import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.StageStyle;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.favourite_programs.Program;

public class RemoveDialog extends Dialog<Boolean> {

    public RemoveDialog(FavouriteProgramsMenu parent) {
        super();
        super.initStyle(StageStyle.UTILITY);

        ListView<Program> programs = new ListView<>();
        programs.getItems().setAll(FavouritePrograms.getPrograms());
        programs.setCellFactory(param -> new ListCell<Program>() {
            @Override
            protected void updateItem(Program item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getName());
                } else {
                    setText("");
                }
            }
        });
        programs.setOnKeyPressed(event -> {
            if (KeyCode.DELETE.equals(event.getCode())) {
                Program selectedItem = programs.getSelectionModel().getSelectedItem();
                programs.getItems().remove(selectedItem);
                FavouritePrograms.remove(selectedItem);
                parent.invalidate();
            }
        });

        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK);
        super.getDialogPane().setContent(programs);
        super.showAndWait();
    }
}
