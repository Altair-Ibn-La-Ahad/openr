package pl.greywarden.openr.configuration;

public enum Settings {
    LEFT_DIR("LEFT_DIR"),
    RIGHT_DIR("RIGHT_DIR"),
    LANGUAGE("LANGUAGE");

    public final String CODE;

    Settings(String code) {
        this.CODE = code;
    }
}
