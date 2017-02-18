package pl.greywarden.openr.gui.dialogs.putty;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.dialogs.CommonButtons;

import java.io.File;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class SetPuttyPathDialog extends Dialog<Boolean> {

    private final TextField pathInput = new TextField();

    public SetPuttyPathDialog() {
        super();
        createAndShowDialog();
    }

    public SetPuttyPathDialog(String actualPath) {
        pathInput.setText(actualPath == null ? "" : actualPath);
        createAndShowDialog();
    }

    private void createAndShowDialog() {
        super.setTitle(getString("putty-path"));
        GridPane layout = new GridPane();
        layout.setHgap(5);
        layout.setMinWidth(500);

        Label pathLabel = new Label(getString("path"));
        HBox pathInputWrapper = new HBox(5);
        Button pathChooser = createPathChooserButton();
        pathInputWrapper.getChildren().setAll(pathInput, pathChooser);

        HBox.setHgrow(pathInput, Priority.ALWAYS);
        GridPane.setHgrow(pathInputWrapper, Priority.ALWAYS);

        layout.addRow(0, pathLabel, pathInputWrapper);

        super.getDialogPane().getButtonTypes().setAll(CommonButtons.OK, CommonButtons.CANCEL);
        super.setResultConverter(button -> CommonButtons.OK.equals(button));
        super.getDialogPane().lookupButton(CommonButtons.OK).disableProperty().bind(pathInput.textProperty().isEmpty());

        super.getDialogPane().setContent(layout);
        pathInput.requestFocus();
        super.showAndWait().ifPresent(confirmed -> ConfigManager.setProperty(Setting.PUTTY, pathInput.getText()));
    }

    private Button createPathChooserButton() {
        Button pathChooser = new Button("...");
        pathChooser.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File puttyDirectory = new File(pathInput.getText());
            if (puttyDirectory.exists()) {
                fileChooser.setInitialDirectory(puttyDirectory.isFile()
                        ? puttyDirectory.getParentFile() : puttyDirectory);
            }
            File result = fileChooser.showOpenDialog(super.getOwner());
            pathInput.setText(result != null ? result.getAbsolutePath() : pathInput.getText());
        });
        return pathChooser;
    }
}
