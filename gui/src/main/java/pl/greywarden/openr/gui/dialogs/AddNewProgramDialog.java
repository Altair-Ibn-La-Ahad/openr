package pl.greywarden.openr.gui.dialogs;

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
import pl.greywarden.openr.gui.favourite_programs.FavouritePrograms;
import pl.greywarden.openr.gui.favourite_programs.ProgramWrapper;
import pl.greywarden.openr.gui.menu.favourite_programs.FavouriteProgramsMenu;

import java.io.File;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class AddNewProgramDialog extends Dialog<Boolean> {

    private TextField nameInput;
    private TextField pathInput;
    private TextField iconInput;

    private final FavouriteProgramsMenu parent;

    public AddNewProgramDialog(FavouriteProgramsMenu parent) {
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
        String iconPath = iconInput.getText();
        FavouritePrograms.add(new ProgramWrapper(name, path, iconPath));
        parent.invalidate();
    }

    private void createDialog() {
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(5);

        Label nameLabel = new Label(getString("name"));
        Label pathLabel = new Label(getString("path"));
        Label iconLabel = new Label(getString("icon"));

        nameInput = new TextField();
        pathInput = new TextField();
        iconInput = new TextField();

        HBox pathInputWrapper = createPathInputWrapper();
        HBox iconInputWrapper = createIconInputWrapper();

        layout.addRow(0, nameLabel, nameInput);
        layout.addRow(1, pathLabel, pathInputWrapper);
        layout.addRow(2, iconLabel, iconInputWrapper);

        GridPane.setHgrow(pathInputWrapper, Priority.ALWAYS);
        GridPane.setHgrow(nameInput, Priority.ALWAYS);

        layout.setMinWidth(500);

        getOkButton().disableProperty().bind(
                nameInput.textProperty().isEmpty().or(pathInput.textProperty().isEmpty()));
        super.getDialogPane().setContent(layout);
    }

    private HBox createPathInputWrapper() {
        HBox pathInputWrapper = new HBox();
        Button showProgramChooser = createProgramChooserButton();
        HBox.setHgrow(pathInput, Priority.ALWAYS);
        pathInputWrapper.getChildren().setAll(pathInput, showProgramChooser);
        return pathInputWrapper;
    }

    private HBox createIconInputWrapper() {
        HBox iconInputWrapper = new HBox();
        Button showIconChooser = createImageChooserButton();
        HBox.setHgrow(iconInput, Priority.ALWAYS);
        iconInputWrapper.getChildren().setAll(iconInput, showIconChooser);
        return iconInputWrapper;
    }

    private Button createImageChooserButton() {
        Button showIconChooser = new Button("...");
        FileChooser.ExtensionFilter[] filters = getImageExtensionFilters();
        showIconChooser.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().setAll(filters);
            File result = fileChooser.showOpenDialog(super.getOwner());
            iconInput.setText(result != null ? result.getAbsolutePath() : iconInput.getText());
        });
        HBox.setMargin(showIconChooser, new Insets(0, 0, 0, 5));
        return showIconChooser;
    }

    private FileChooser.ExtensionFilter[] getImageExtensionFilters() {
        return new FileChooser.ExtensionFilter[] {
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
        };
    }

    private Button createProgramChooserButton() {
        Button showProgramChooser = new Button("...");
        showProgramChooser.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File result = fileChooser.showOpenDialog(this.getOwner());
            pathInput.setText(result != null ? result.getAbsolutePath() : pathInput.getText());
        });
        HBox.setMargin(showProgramChooser, new Insets(0, 0, 0, 5));
        return showProgramChooser;
    }

    private Node getOkButton() {
        return super.getDialogPane().lookupButton(CommonButtons.OK);
    }
}
