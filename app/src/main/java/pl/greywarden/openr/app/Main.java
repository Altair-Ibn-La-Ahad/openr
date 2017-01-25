package pl.greywarden.openr.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.dialogs.ConfirmExitDialog;
import pl.greywarden.openr.gui.scenes.MainWindow;

import java.util.Optional;

public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        Platform.runLater(() -> new MainWindow().show());
    }
}
