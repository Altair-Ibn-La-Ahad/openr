package pl.greywarden.openr.gui.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.favourite_programs.ProgramWrapper;
import pl.greywarden.openr.gui.menu.favourite_programs.FavouriteProgramsMenu;

public class ModifyProgramsDialog extends Dialog<Boolean> {

    private final FavouriteProgramsMenu parent;

    public ModifyProgramsDialog(FavouriteProgramsMenu parent) {
        super();
        super.initStyle(StageStyle.UTILITY);

        this.parent = parent;

        ListView<ProgramWrapper> programs = createProgramsListView();

        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK);
        super.getDialogPane().setContent(programs);
        super.showAndWait();
    }

    private ListView<ProgramWrapper> createProgramsListView() {
        ListView<ProgramWrapper> programs = new ListView<>();
        programs.getItems().setAll(FavouritePrograms.getPrograms());
        programs.setCellFactory(this::createProgramListCell);
        programs.setOnKeyPressed(event -> handleKeyPressed(programs, event));
        programs.setStyle("-fx-padding: 0");
        return programs;
    }

    private void handleKeyPressed(ListView<ProgramWrapper> programsList, KeyEvent event) {
        if (KeyCode.DELETE.equals(event.getCode())) {
            removeSelectedItem(programsList);
        }
        if (KeyCode.ESCAPE.equals(event.getCode())) {
            programsList.getSelectionModel().clearSelection();
        }
    }

    private void removeSelectedItem(ListView<ProgramWrapper> programs) {
        ProgramWrapper selectedItem = programs.getSelectionModel().getSelectedItem();
        programs.getItems().remove(selectedItem);
        FavouritePrograms.remove(selectedItem);
        parent.invalidate();
    }

    private ListCell<ProgramWrapper> createProgramListCell(ListView<ProgramWrapper> programs) {
        return new ListCell<ProgramWrapper>() {
            @Override
            protected void updateItem(ProgramWrapper item, boolean empty) {
                super.updateItem(item, empty);
                HBox wrapper = new HBox();
                if (!empty) {
                    ImageView programIcon = createProgramIcon(item);
                    Button remove = createRemoveButton(item);
                    Label name = new Label(item.getName());
                    Pane spacer = new Pane();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    wrapper.setAlignment(Pos.CENTER);
                    wrapper.getChildren().setAll(programIcon, name, spacer, remove);
                }
                super.setGraphic(wrapper);
            }

            private ImageView createProgramIcon(ProgramWrapper item) {
                ImageView programIcon = IconManager.getBigIconFromPath(item.getIcon());
                HBox.setMargin(programIcon, new Insets(0, 5, 0, 0));
                return programIcon;
            }

            private Button createRemoveButton(ProgramWrapper item) {
                Button remove = new Button();
                remove.setGraphic(IconManager.getProgramIcon("delete-permanent-small"));
                remove.setOnAction(event -> removeProgramFromList(item));
                return remove;
            }

            private void removeProgramFromList(ProgramWrapper item) {
                programs.getItems().remove(item);
                FavouritePrograms.remove(item);
                parent.invalidate();
            }
        };
    }
}
