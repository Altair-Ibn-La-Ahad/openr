package pl.greywarden.openr.gui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import pl.greywarden.openr.gui.directoryview.DirectoryView;

import java.io.File;

public class PathTextField extends TextField {

    public PathTextField(DirectoryView directoryView) {
        super();
        super.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (getEnteredDirectory() != null) {
                    directoryView.changePath(getEnteredDirectory().getAbsolutePath());
                }
            }
        });
    }

    private File getEnteredDirectory() {
        File directory = new File(getText());
        return directory.exists() && directory.isDirectory() ? directory : null;
    }

}
