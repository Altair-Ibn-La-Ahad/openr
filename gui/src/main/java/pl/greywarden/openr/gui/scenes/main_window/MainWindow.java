package pl.greywarden.openr.gui.scenes.main_window;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Settings;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.dialogs.ConfirmExitDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.CentralContainter;
import pl.greywarden.openr.commons.I18nManager;

import java.util.Optional;

@Log4j
public class MainWindow extends Stage {

    @Getter
    private static final MainWindowStatusBar statusBar = new MainWindowStatusBar();
    private static final CentralContainter centralContainter = new CentralContainter();
    private final VBox layout = new VBox();

    private static MainWindow instance;

    public static MainWindow getInstance() {
        return instance == null ? instance = new MainWindow() : instance;
    }

    private MainWindow() {
        I18nManager.setLocale(ConfigManager.getSetting(Settings.LANGUAGE.CODE));
        Scene scene = new Scene(layout);
        buildScene();

        super.setTitle("OpenR " + AboutDialog.getVersion());
        super.setScene(scene);
        super.setMaximized(true);

        super.setOnCloseRequest(event -> {
            Optional<ButtonType> confirm = new ConfirmExitDialog().showAndWait();
            if (confirm.isPresent()) {
                if (ButtonBar.ButtonData.YES.equals(confirm.get().getButtonData())) {
                    ConfigManager.setProperty(Settings.LEFT_DIR.CODE,
                            getLeftDirectoryView().getRootPath());
                    ConfigManager.setProperty(Settings.RIGHT_DIR.CODE,
                            getRightDirectoryView().getRootPath());
                    ConfigManager.setProperty(Settings.LANGUAGE.CODE,
                            I18nManager.getActualLocale().getLanguage());
                    ConfigManager.storeSettings();
                    Platform.exit();
                }
            }
            event.consume();
        });
    }

    private void buildScene() {
        layout.getChildren().addAll(
                new MainWindowMenuBar(),
                new MainWindowToolBar(),
                centralContainter,
                statusBar);
    }

    public static DirectoryView getLeftDirectoryView() {
        return centralContainter.getLeftView().getDirectoryView();
    }

    public static DirectoryView getRightDirectoryView() {
        return centralContainter.getRightView().getDirectoryView();
    }
}
