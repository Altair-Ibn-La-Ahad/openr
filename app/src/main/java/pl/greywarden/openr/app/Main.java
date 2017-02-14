package pl.greywarden.openr.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pl.greywarden.openr.gui.main_window.MainWindow;

public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        Platform.runLater(() -> MainWindow.getInstance().show());
    }
}
