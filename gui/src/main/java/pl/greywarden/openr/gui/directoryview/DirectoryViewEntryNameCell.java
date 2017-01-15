package pl.greywarden.openr.gui.directoryview;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DirectoryViewEntryNameCell extends TableCell <AbstractEntry, AbstractEntry> {

    @Override
    protected void updateItem(AbstractEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            HBox vb = new HBox();
            vb.setAlignment(Pos.CENTER_LEFT);
            String nameOfEntry = item.getEntryProperties().getBaseName();
            Label nameLabel = new Label(nameOfEntry);
            ImageView iv = new ImageView(getFileIcon(item.getEntryProperties().getAbsolutePath()));
            HBox.setMargin(iv, new Insets(0, 3, 0, (item instanceof ParentDirectoryEntry) ? 0 : 5));
            vb.getChildren().addAll(iv, nameLabel);
            setGraphic(vb);
            Tooltip.install(this, new Tooltip(item.getEntryProperties().getName()));
        }
    }

    private Image getFileIcon(String path) {
        File file = new File(path);
        java.awt.Image awtImage = ((ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file)).getImage();
        BufferedImage bImg;
        if (awtImage instanceof BufferedImage) {
            bImg = (BufferedImage) awtImage;
        } else {
            bImg = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bImg.createGraphics();
            graphics.drawImage(awtImage, 0, 0, null);
            graphics.dispose();
        }
        return SwingFXUtils.toFXImage(bImg, null);
    }

}
