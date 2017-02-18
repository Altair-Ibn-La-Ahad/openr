package pl.greywarden.openr.gui.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.configuration.ConfigManager;
import pl.greywarden.openr.configuration.Setting;
import pl.greywarden.openr.gui.dialogs.find.FindWindow;
import pl.greywarden.openr.gui.dialogs.grep.GrepWindow;
import pl.greywarden.openr.gui.dialogs.putty.SetPuttyPathDialog;

import java.io.IOException;

import static pl.greywarden.openr.commons.I18nManager.getString;

@Log4j
public class Tools extends Menu {

    public Tools() {
        super(getString("tools"));
        createMenu();
    }

    private void createMenu() {
        MenuItem grep = createGrepMenuItem();
        MenuItem find = createFindMenuItem();
        MenuItem putty = createPuttyMenuItem();

        super.getItems().addAll(grep, find, putty);
    }

    private MenuItem createPuttyMenuItem() {
        MenuItem putty = new MenuItem("PuTTY");
        putty.setGraphic(IconManager.getProgramIcon("putty"));
        putty.setOnAction(event -> {
            String puttyPath = ConfigManager.getSetting(Setting.PUTTY);
            if (puttyPath == null) {
                new SetPuttyPathDialog();
            } else {
                try {
                    new ProcessBuilder(puttyPath).start();
                } catch (IOException e) {
                    log.warn("Failed to start PuTTY", e);
                }
            }
        });
        putty.setAccelerator(new KeyCodeCombination(KeyCode.P,
                KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        return putty;
    }

    private MenuItem createFindMenuItem() {
        MenuItem find = new MenuItem(getString("find-file"));
        find.setGraphic(IconManager.getProgramIcon("find"));
        find.setOnAction(event -> new FindWindow());
        find.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        return find;
    }

    private MenuItem createGrepMenuItem() {
        MenuItem grep = new MenuItem(getString("grep-window-title"));
        grep.setGraphic(IconManager.getProgramIcon("grep"));
        grep.setOnAction(event -> new GrepWindow());
        grep.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
        return grep;
    }
}
