package pl.greywarden.openr.gui.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.MalformedURLException;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class PreviewImageDialog extends Stage {

    private ImageView imageView;
    private StackPane imagePane;

    public PreviewImageDialog(File imageFile) {
        super(StageStyle.UTILITY);
        super.setTitle(getString("preview"));
        createPreviewImageDialogLayout(imageFile);
        ScrollPane wrapper = new ScrollPane(imagePane);
        wrapper.addEventFilter(ScrollEvent.SCROLL, this::scaleImagePreview);
        super.setScene(new Scene(wrapper));
        centerOnScreen();
        show();
    }

    private void createPreviewImageDialogLayout(File imageFile) {
        imageView = getImageViewFromFile(imageFile);
        imagePane = createImagePane(imageView);
        imagePane.getChildren().setAll(imageView);
    }

    private void scaleImagePreview(ScrollEvent event) {
        double delta = event.getDeltaY();
        if (canScale(delta)) {
            imageView.setFitWidth(imageView.getFitWidth() + delta);
            imageView.setFitHeight(imageView.getFitHeight() + delta);
            imageView.setPreserveRatio(true);
        }
        event.consume();
    }

    private boolean canScale(double delta) {
        return delta > 0 || imageView.getFitWidth() >= super.getWidth();
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
            scaleToFitWindow(imageView);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return imageView;
    }

    private void scaleToFitWindow(ImageView imageView) {
        Image image = imageView.getImage();
        double scaleFactor = getScaleFactor(image);
        double imageWidth = imageView.getImage().getWidth();
        double imageHeight = imageView.getImage().getHeight();
        imageView.fitWidthProperty().setValue(imageWidth / scaleFactor);
        imageView.fitHeightProperty().setValue(imageHeight / scaleFactor);
    }

    private double getScaleFactor(Image image) {
        int MAX_IMAGE_PREVIEW_WIDTH = 500;
        return image.widthProperty().greaterThan(MAX_IMAGE_PREVIEW_WIDTH).get()
                ? (image.getWidth() / MAX_IMAGE_PREVIEW_WIDTH)
                : 1.0;
    }
}
