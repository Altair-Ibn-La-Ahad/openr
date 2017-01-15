package pl.greywarden.openr.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.greywarden.openr.gui.wrappers.DirectoryViewWrapper;

public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DirectoryViewWrapper directoryViewWrapper = new DirectoryViewWrapper(System.getProperty("user.dir"));
        VBox.setVgrow(directoryViewWrapper, Priority.ALWAYS);
        primaryStage.setScene(new Scene(directoryViewWrapper));
        primaryStage.show();
    }
}
