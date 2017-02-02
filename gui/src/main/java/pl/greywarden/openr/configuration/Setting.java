package pl.greywarden.openr.configuration;

public enum Setting {
    LEFT_DIR("LEFT_DIR"),
    RIGHT_DIR("RIGHT_DIR"),
    LANGUAGE("LANGUAGE"),
    KEEP_CLIPBOARD("KEEP_CLIPBOARD"),
    CONFIRM_CLOSE("CONFIRM_CLOSE");

    public final String CODE;

    Setting(String code) {
        this.CODE = code;
    }
}
