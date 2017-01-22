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
import pl.greywarden.openr.gui.scenes.MainWindowScene;

import java.util.Optional;

public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("OpenR " + AboutDialog.getVersion());
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
