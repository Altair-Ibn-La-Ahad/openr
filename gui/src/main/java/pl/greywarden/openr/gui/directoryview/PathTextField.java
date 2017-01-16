package pl.greywarden.openr.gui.directoryview;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.event.EventType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.File;

public class PathTextField extends TextField {

    private DirectoryView directoryView;

    private String lastPath;

    @SuppressWarnings("unchecked")
    public PathTextField(DirectoryView directoryView) {
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
                super.textProperty().setValue(lastPath);
            }
        });
        this.directoryView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            this.textProperty().setValue(directoryView.getRootPath());
            super.positionCaret(getText().length());
            lastPath = directoryView.getRootPath();
        });
        super.textProperty().addListener((observable, oldValue, newValue) -> {
            File enteredDirectoryPath = getEnteredDirectory(newValue);
            super.setStyle(enteredDirectoryPath.exists() && enteredDirectoryPath.isDirectory()
                    ? null : "-fx-text-fill: red");
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
