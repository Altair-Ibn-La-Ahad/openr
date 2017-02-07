package pl.greywarden.openr.gui.dialogs;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class CommonButtons {

    public static final ButtonType OK = new ButtonType(getString("ok"), ButtonBar.ButtonData.OK_DONE);
    public static final ButtonType CANCEL = new ButtonType(getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
    public static final ButtonType APPLY = new ButtonType(getString("apply"), ButtonBar.ButtonData.APPLY);
    public static final ButtonType YES = new ButtonType(getString("yes"), ButtonBar.ButtonData.YES);
    public static final ButtonType NO = new ButtonType(getString("no"), ButtonBar.ButtonData.NO);
}
