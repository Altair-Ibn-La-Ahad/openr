package pl.greywarden.openr.configuration;

public enum Setting {
    LEFT_DIR("LEFT_DIR"),
    RIGHT_DIR("RIGHT_DIR"),
    LANGUAGE("LANGUAGE"),
    KEEP_CLIPBOARD("KEEP_CLIPBOARD"),
    CONFIRM_CLOSE("CONFIRM_CLOSE"),
    LEFT_VIEW_VISIBLE("LEFT_VIEW_VISIBLE"),
    RIGHT_VIEW_VISIBLE("RIGHT_VIEW_VISIBLE"),
    TOOL_BAR_VISIBLE("TOOL_BAR_VISIBLE"),
    STATUS_BAR_VISIBLE("STATUS_BAR_VISIBLE"),
    HIDDEN_FILES_VISIBLE("HIDDEN_FILES_VISIBLE");

    public final String CODE;

    Setting(String code) {
        this.CODE = code;
    }
}
