package pl.greywarden.openr.gui.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.StatusBar;
import pl.greywarden.openr.i18n.I18nManager;

public class MainWindowStatusBar extends StatusBar {

    public MainWindowStatusBar() {
        super();
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("menu-bar");

        Label label = new Label(i18n.getString("chosen-file"));
        TextField pathTextField = new TextField();

        label.setPadding(new Insets(0, 5, 0, 5));
        HBox.setHgrow(label, Priority.ALWAYS);

        pathTextField.setId("statusbar-text-field");

        HBox wrapper = new HBox();
        HBox.setHgrow(pathTextField, Priority.ALWAYS);
        wrapper.prefWidthProperty().bind(super.widthProperty().divide(2));
        wrapper.setAlignment(Pos.CENTER);
        wrapper.getChildren().addAll(label, pathTextField);

        super.setText("OpenR");
        pathTextField.setEditable(false);
        super.getRightItems().add(wrapper);
    }

}
