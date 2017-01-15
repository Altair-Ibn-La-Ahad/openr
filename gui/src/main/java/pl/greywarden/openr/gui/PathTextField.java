package pl.greywarden.openr.gui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.io.File;

public class PathTextField extends TextField {

    private DirectoryView directoryView;

    public PathTextField(DirectoryView directoryView) {
        super();
        this.directoryView = directoryView;
        File root = new File(directoryView.getRootPath());
        this.textProperty().setValue(root.getAbsolutePath());
        super.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                goToEnteredDirectory();
            }
        });
        super.textProperty().addListener((observable, oldValue, newValue) -> {
            super.setStyle(getEnteredDirectory(newValue).exists() ? null : "-fx-text-fill: red");
        });
    }

    public void goToEnteredDirectory() {
        File enteredPath = getEnteredDirectory(getText());
        if (enteredPath.isDirectory()) {
            directoryView.changePath(enteredPath.getAbsolutePath());
            File rootFile = new File(directoryView.getRootPath());
            this.textProperty().setValue(rootFile.getAbsolutePath());
            super.positionCaret(getText().length());
        }
    }

    private File getEnteredDirectory(String path) {
        return new File(path);
    }

}
