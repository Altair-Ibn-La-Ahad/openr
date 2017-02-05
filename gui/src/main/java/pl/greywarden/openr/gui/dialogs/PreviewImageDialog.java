package pl.greywarden.openr.gui.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.MalformedURLException;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class PreviewImageDialog extends Stage {

    public PreviewImageDialog(File imageFile) {
        super();
        super.setTitle(getString("preview"));
        super.initStyle(StageStyle.UTILITY);
        ImageView imageView = getImageViewFromFile(imageFile);
        StackPane imagePane = createImagePane(imageView);
        scaleToFitWindow(imageView);
        imagePane.getChildren().setAll(imageView);
        super.setScene(new Scene(imagePane));
        centerOnScreen();
    }

    private StackPane createImagePane(ImageView imageView) {
        StackPane imagePane = new StackPane();
        StackPane.setAlignment(imageView, Pos.CENTER);
        imagePane.setPadding(new Insets(5));
        imagePane.setMinSize(150, 150);
        return imagePane;
    }

    private ImageView getImageViewFromFile(File imageFile) {
        ImageView imageView;
        try {
            imageView = new ImageView(imageFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return imageView;
    }

    private void scaleToFitWindow(ImageView imageView) {
        Image image = imageView.getImage();
        double scaleFactor = image.widthProperty().greaterThan(500).get()
                ? image.getWidth() / 500.0
                : 1.0;
        double imageWidth = imageView.getImage().getWidth();
        double imageHeight = imageView.getImage().getHeight();
        imageView.fitWidthProperty().setValue(imageWidth / scaleFactor);
        imageView.fitHeightProperty().setValue(imageHeight / scaleFactor);
    }
}
