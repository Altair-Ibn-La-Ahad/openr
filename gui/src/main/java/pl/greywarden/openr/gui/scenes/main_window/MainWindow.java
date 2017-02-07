package pl.greywarden.openr.gui.scenes.main_window;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.dialogs.ConfirmExitDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.directoryview.DirectoryViewWrapper;
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
        createWindow();
    }

    public void reload() {
        super.close();
        createWindow();
        super.show();
    }

    private void createWindow() {
        I18nManager.setLocale(ConfigManager.getSetting(Setting.LANGUAGE.CODE));
        Scene scene = new Scene(layout);
        KeyCodeCombination NEW_FILE_SHORTCUT = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY);
        scene.getAccelerators().put(NEW_FILE_SHORTCUT, () -> new CreateFileDialog().showDialog());
        buildScene();

        super.setTitle("OpenR " + AboutDialog.getVersion());
        super.setScene(scene);
        super.setMaximized(true);

        super.setOnCloseRequest(event -> {
            if (Boolean.valueOf(ConfigManager.getSetting(Setting.CONFIRM_CLOSE.CODE))) {
                Optional<ButtonType> confirm = new ConfirmExitDialog().showAndWait();
                if (confirm.isPresent()) {
                    if (ButtonBar.ButtonData.YES.equals(confirm.get().getButtonData())) {
                        storeSettingsAndExit();
                    }
                }
                event.consume();
            } else {
                storeSettingsAndExit();
            }
        });
    }

    private void storeSettingsAndExit() {
        ConfigManager.setProperty(Setting.LEFT_DIR.CODE,
                getLeftDirectoryView().getRootPath());
        ConfigManager.setProperty(Setting.RIGHT_DIR.CODE,
                getRightDirectoryView().getRootPath());
        ConfigManager.storeSettings();
        Platform.exit();
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

    public static DirectoryViewWrapper getLeftWrapper() {
        return centralContainter.getLeftView();
    }

    public static DirectoryViewWrapper getRightWrapper() {
        return centralContainter.getRightView();
    }

}
