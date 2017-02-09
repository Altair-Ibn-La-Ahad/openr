package pl.greywarden.openr.gui.property_editor.table_view;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Map;

public class PropertyWrapper {

    private final SimpleStringProperty key;
    private final SimpleStringProperty value;

    public PropertyWrapper(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public PropertyWrapper(Map.Entry entry) {
        this(entry.getKey().toString(), entry.getValue().toString());
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getKey() {
        return this.key.get();
    }

    public String getEscapedKey() {
        return StringEscapeUtils.escapeJava(getKey());
    }

    public String getValue() {
        return this.value.get();
    }

    public String getEscapedValue() {
        return StringEscapeUtils.escapeJava(getValue());
    }
}
