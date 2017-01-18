package pl.greywarden.openr.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.greywarden.openr.gui.dialogs.ConfirmExitDialog;
import pl.greywarden.openr.gui.scenes.MainWindowScene;
import pl.greywarden.openr.i18n.I18nManager;

import java.util.Optional;

public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("default");
        primaryStage.setTitle(i18n.getString("main-window-title"));
        Platform.setImplicitExit(false);
        MainWindowScene mainWindowScene = new MainWindowScene();
        primaryStage.setScene(new Scene(mainWindowScene));
        primaryStage.setMaximized(true);
        primaryStage.show();

        Window mainWindow = primaryStage.getScene().getWindow();
        mainWindow.setOnCloseRequest(event -> {
            Optional<ButtonType> confirm = new ConfirmExitDialog().showAndWait();
            if (confirm.isPresent()) {
                if (ButtonBar.ButtonData.YES.equals(confirm.get().getButtonData())) {
                    Platform.exit();
                }
            }
            event.consume();
        });
    }
}
