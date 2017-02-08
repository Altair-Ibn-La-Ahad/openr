package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class CommonButtons {

    public static ButtonType OK = new ButtonType(getString("ok"), ButtonBar.ButtonData.OK_DONE);
    public static ButtonType CANCEL = new ButtonType(getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
    public static ButtonType APPLY = new ButtonType(getString("apply"), ButtonBar.ButtonData.APPLY);
    public static ButtonType YES = new ButtonType(getString("yes"), ButtonBar.ButtonData.YES);
    public static ButtonType NO = new ButtonType(getString("no"), ButtonBar.ButtonData.NO);

    public static void reinitialize() {
        OK = new ButtonType(getString("ok"), ButtonBar.ButtonData.OK_DONE);
        CANCEL = new ButtonType(getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        APPLY = new ButtonType(getString("apply"), ButtonBar.ButtonData.APPLY);
        YES = new ButtonType(getString("yes"), ButtonBar.ButtonData.YES);
        NO = new ButtonType(getString("no"), ButtonBar.ButtonData.NO);
    }
}
