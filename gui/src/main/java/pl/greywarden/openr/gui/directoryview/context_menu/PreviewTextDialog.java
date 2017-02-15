package pl.greywarden.openr.gui.directoryview.context_menu;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class PreviewTextDialog extends Stage {

    public PreviewTextDialog(File selectedFile) {
        super(StageStyle.UTILITY);
        super.setTitle(getString("preview"));

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        try {
            InputStream inputStream = new FileInputStream(selectedFile);
            textArea.setText(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            textArea.setText("");
        }
        super.setScene(new Scene(textArea));
        textArea.setMinSize(500, 500);
        centerOnScreen();
        show();
    }
}
