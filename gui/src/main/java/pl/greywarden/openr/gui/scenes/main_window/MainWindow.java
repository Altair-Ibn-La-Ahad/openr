package pl.greywarden.openr.gui.scenes.main_window;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.commons.I18nManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.create_file.CreateFileDialog;
import pl.greywarden.openr.gui.dialogs.AboutDialog;
import pl.greywarden.openr.gui.dialogs.ConfirmExitDialog;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.directoryview.DirectoryViewWrapper;
import pl.greywarden.openr.gui.scenes.CentralContainter;

@Log4j
public class MainWindow extends Stage {

    @Getter
    private static MainWindowStatusBar statusBar;
    private static CentralContainter centralContainter;
    private VBox layout;

    private static MainWindow instance;
    @Getter
    private static MainWindowToolBar mainWindowToolBar;

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
        layout = new VBox();
        Scene scene = new Scene(layout);
        KeyCodeCombination NEW_FILE_SHORTCUT = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY);
        scene.getAccelerators().put(NEW_FILE_SHORTCUT, () -> new CreateFileDialog().showDialog());
        buildScene();

        super.setTitle("OpenR " + AboutDialog.getVersion());
        super.setScene(scene);
        super.setMaximized(true);

        super.setOnCloseRequest(ConfirmExitDialog::new);
    }

    private void buildScene() {
        mainWindowToolBar = new MainWindowToolBar();
        statusBar = new MainWindowStatusBar();
        centralContainter = new CentralContainter();
        layout.getChildren().addAll(
                new MainWindowMenuBar(),
                mainWindowToolBar,
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
