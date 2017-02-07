package pl.greywarden.openr.gui.directoryview;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.File;

public class DirectoryViewPathTextField extends TextField {

    private final DirectoryView directoryView;

    private String lastPath;

    @SuppressWarnings("unchecked")
    public DirectoryViewPathTextField(DirectoryView directoryView) {
        super();
        this.directoryView = directoryView;
        File root = new File(directoryView.getRootPath());
        this.textProperty().setValue(root.getAbsolutePath());
        this.lastPath = textProperty().getValue();
        super.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                goToEnteredDirectory();
            }
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                restorePreviousPath();
            }
        });
        updateTextInPathInput(directoryView);
        markTextWhenEnteredPathNotValid();
    }

    private void restorePreviousPath() {
        super.textProperty().setValue(lastPath);
    }

    private void updateTextInPathInput(DirectoryView directoryView) {
        this.directoryView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            this.textProperty().setValue(directoryView.getRootPath());
            super.positionCaret(getText().length());
            lastPath = directoryView.getRootPath();
        });
    }

    private void markTextWhenEnteredPathNotValid() {
        super.textProperty().addListener((observable, oldValue, newValue) -> {
            File enteredDirectoryPath = getEnteredDirectory(newValue);
            String RED_TEXT = "-fx-text-fill: red";
            super.setStyle(enteredDirectoryPath.exists() && enteredDirectoryPath.isDirectory()
                    ? null : RED_TEXT);
        });
    }

    public void goToEnteredDirectory() {
        File enteredPath = getEnteredDirectory(getText());
        if (enteredPath.isDirectory()) {
            directoryView.changePath(enteredPath.getAbsolutePath());
            File rootFile = new File(directoryView.getRootPath());
            this.textProperty().setValue(rootFile.getAbsolutePath());
            super.positionCaret(getText().length());
            lastPath = enteredPath.getAbsolutePath();
        }
    }

    private File getEnteredDirectory(String path) {
        return new File(path);
    }

}
