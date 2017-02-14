package pl.greywarden.openr.gui.main_window;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.directoryview.DirectoryViewWrapper;

import static pl.greywarden.openr.configuration.ConfigManager.getSetting;

public class CentralContainter extends HBox {

    @Getter
    private final DirectoryViewWrapper leftView;
    @Getter
    private final DirectoryViewWrapper rightView;

    public CentralContainter() {
        leftView = createLeftView();
        rightView = createRightView();

        super.getChildren().addAll(leftView, rightView);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private DirectoryViewWrapper createRightView() {
        DirectoryViewWrapper right = new DirectoryViewWrapper(getSetting(Setting.RIGHT_DIR));
        right.managedProperty().setValue(Boolean.valueOf(getSetting(Setting.RIGHT_VIEW_VISIBLE)));
        right.minWidthProperty().bind(super.widthProperty().multiply(0.5));
        HBox.setHgrow(right, Priority.ALWAYS);
        return right;
    }

    private DirectoryViewWrapper createLeftView() {
        DirectoryViewWrapper left = new DirectoryViewWrapper(getSetting(Setting.LEFT_DIR));
        left.managedProperty().setValue(Boolean.valueOf(getSetting(Setting.LEFT_VIEW_VISIBLE)));
        left.managedProperty().addListener((observable, oldValue, newValue) ->
                ConfigManager.setProperty(Setting.LEFT_VIEW_VISIBLE, newValue));
        left.minWidthProperty().bind(super.widthProperty().multiply(0.5));
        String delimiter = "-fx-border-color: gray;\n" +
                "-fx-border-width: 0 2 0 0;\n";
        left.setStyle(delimiter);
        HBox.setHgrow(left, Priority.ALWAYS);
        return left;
    }

}
