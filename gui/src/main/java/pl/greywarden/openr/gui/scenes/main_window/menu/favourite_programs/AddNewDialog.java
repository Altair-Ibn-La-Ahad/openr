package pl.greywarden.openr.gui.scenes.main_window.menu.favourite_programs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import pl.greywarden.openr.gui.dialogs.CommonButtons;
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.favourite_programs.Program;

import java.io.File;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class AddNewDialog extends Dialog<Boolean> {

    private TextField nameInput;
    private TextField pathInput;

    private final FavouriteProgramsMenu parent;

    public AddNewDialog(FavouriteProgramsMenu parent) {
        super();
        super.initStyle(StageStyle.UTILITY);
        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK, CommonButtons.CANCEL);
        this.parent = parent;

        createDialog();
        super.setResultConverter(param -> CommonButtons.OK.equals(param));
        nameInput.requestFocus();
        super.showAndWait().ifPresent(confirmed -> {
            if (confirmed) {
                handleConfirm();
            }
        });
    }

    private void handleConfirm() {
        String name = nameInput.getText();
        String path = pathInput.getText();

        FavouritePrograms.add(new Program(name, path));
        parent.invalidate();
    }

    private void createDialog() {
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(5);

        Label nameLabel = new Label(getString("name"));
        Label pathLabel = new Label(getString("path"));

        nameInput = new TextField();
        pathInput = new TextField();

        HBox pathInputWrapper = new HBox();
        Button showProgramChooser = new Button("...");
        showProgramChooser.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File result = fileChooser.showOpenDialog(this.getOwner());
            pathInput.setText(result != null ? result.getAbsolutePath() : pathInput.getText());
        });
        HBox.setHgrow(pathInput, Priority.ALWAYS);
        HBox.setMargin(showProgramChooser, new Insets(0, 0, 0, 5));
        pathInputWrapper.getChildren().setAll(pathInput, showProgramChooser);

        layout.addRow(0, nameLabel, nameInput);
        layout.addRow(1, pathLabel, pathInputWrapper);

        GridPane.setHgrow(pathInputWrapper, Priority.ALWAYS);
        GridPane.setHgrow(nameInput, Priority.ALWAYS);

        layout.setMinWidth(500);

        getOkButton().disableProperty().bind(
                nameInput.textProperty().isEmpty().or(pathInput.textProperty().isEmpty()));
        super.getDialogPane().setContent(layout);
    }

    private Node getOkButton() {
        return super.getDialogPane().lookupButton(CommonButtons.OK);
    }
}
