package pl.greywarden.openr.gui.scenes.main_window.menu.favourite_programs;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;
import pl.greywarden.openr.commons.IconManager;
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
                    HBox wrapper = new HBox();
                    Pane spacer = new Pane();
                    Label label = new Label(item.getName());
                    Button removeButton = new Button();
                    removeButton.setGraphic(IconManager.getIcon("delete-permanent-small"));
                    removeButton.setOnAction(event -> {
                        FavouritePrograms.remove(item);
                        parent.invalidate();
                        programs.getItems().remove(item);
                    });
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    wrapper.setAlignment(Pos.CENTER);
                    wrapper.getChildren().setAll(label, spacer, removeButton);
                    setGraphic(wrapper);
                }
            }
        });

        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK);
        super.getDialogPane().setContent(programs);
        super.showAndWait();
    }
}
