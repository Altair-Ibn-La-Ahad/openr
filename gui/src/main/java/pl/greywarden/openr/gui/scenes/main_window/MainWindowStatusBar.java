package pl.greywarden.openr.gui.scenes.main_window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;
import org.controlsfx.control.StatusBar;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;

import static pl.greywarden.openr.commons.I18nManager.getString;
import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class MainWindowStatusBar extends StatusBar {

    @Getter
    private final TextField pathTextField;

    public MainWindowStatusBar() {
        super();

        super.managedProperty().setValue(Boolean.valueOf(getSetting(Setting.STATUS_BAR_VISIBLE)));

        super.managedProperty().addListener((observable, oldValue, newValue) ->
                ConfigManager.setProperty(Setting.STATUS_BAR_VISIBLE, newValue));

        Label label = new Label(getString("status-bar-selected-file"));
        pathTextField = new TextField();

        label.setPadding(new Insets(0, 5, 0, 5));
        HBox.setHgrow(label, Priority.ALWAYS);

        HBox wrapper = new HBox();
        HBox.setHgrow(pathTextField, Priority.ALWAYS);
        wrapper.prefWidthProperty().bind(super.widthProperty().divide(2));
        wrapper.setAlignment(Pos.CENTER);
        wrapper.getChildren().addAll(label, pathTextField);

        super.setText("OpenR");
        pathTextField.setEditable(false);
        super.getRightItems().add(wrapper);
        super.visibleProperty().bind(managedProperty());
    }

}
