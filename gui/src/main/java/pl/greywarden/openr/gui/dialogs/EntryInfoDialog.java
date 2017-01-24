package pl.greywarden.openr.gui.dialogs;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.i18n.I18nManager;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class EntryInfoDialog extends Dialog {

    private final File selectedEntry;
    private final I18nManager i18n = I18nManager.getInstance();

    public EntryInfoDialog(String pathToEntry) {
        super();
        i18n.setBundle("entry-info");
        selectedEntry = new File(pathToEntry);

        createInfo();
        super.getDialogPane().getButtonTypes().add(new ButtonType(i18n.getString("ok"), ButtonBar.ButtonData.OK_DONE));
        super.setGraphic(new ImageView(getFileIcon(pathToEntry)));
        super.setTitle(selectedEntry.getName());
        super.getDialogPane().minWidthProperty().setValue(600);
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

    private void createInfo() {
        HBox wrapper = new HBox();
        AbstractEntry entry = selectedEntry.isFile()
                ? new FileEntry(selectedEntry.getAbsolutePath())
                : new DirectoryEntry(selectedEntry.getAbsolutePath());

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        Label nameLabel = new Label(i18n.getString("name") + ":");
        Label absolutePathLabel = new Label(i18n.getString("absolute-path") + ":");
        Label typeLabel = new Label(i18n.getString("type") + ":");
        Label privilegesLabel = new Label(i18n.getString("privileges") + ":");
        Label sizeLabel = new Label(i18n.getString("size") + ":");

        TextField name = new TextField();
            name.setEditable(false);
        TextField absolutePath = new TextField();
            absolutePath.setEditable(false);
        TextField type = new TextField();
            type.setEditable(false);
        TextField privileges = new TextField();
            privileges.setEditable(false);
        TextField size = new TextField();
            size.setEditable(false);

        grid.addRow(0, nameLabel, name);
        grid.addRow(1, absolutePathLabel, absolutePath);
        grid.addRow(2, sizeLabel, size);
        grid.addRow(3, typeLabel, type);
        type.setText(entry.getEntryProperties().getMimeType());

        if (selectedEntry.isFile()) {
            grid.addRow(4, privilegesLabel, privileges);
            if (SystemUtils.IS_OS_WINDOWS) {
                privileges.setText(entry.getEntryProperties().getDosFilePermissions());
            } else {
                privileges.setText(entry.getEntryProperties().getPosixFilePermissions());
            }
        }

        name.setText(entry.getEntryProperties().getName());
        absolutePath.setText(entry.getEntryProperties().getAbsolutePath());
        size.setText(FileUtils.byteCountToDisplaySize(entry.getEntryProperties().getSizeInBytes()));

        HBox.setHgrow(grid, Priority.ALWAYS);
        GridPane.setHgrow(absolutePath, Priority.ALWAYS);
        wrapper.getChildren().add(grid);

        super.getDialogPane().setContent(grid);
    }

}
