package pl.greywarden.openr.gui.dialogs.property_editor;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import pl.greywarden.openr.gui.dialogs.property_editor.table_view.PropertiesTableView;
import pl.greywarden.openr.gui.dialogs.property_editor.table_view.PropertyWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

public class PropertyEditorDialog extends Stage {

    private final File file;
    @Getter
    private PropertiesTableView tableView;

    public final BooleanBinding propertiesEdited = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            return tableView.editedProperty.get();
        }
    };
    @Getter
    private FilteredList<PropertyWrapper> filteredProperties;

    public PropertyEditorDialog(File file) {
        super();
        super.setTitle(file.getAbsolutePath());
        this.file = file;
        VBox layout = createLayout();
        loadPropertiesIntoTable();

        super.initStyle(StageStyle.UTILITY);
        super.setScene(new Scene(layout));
        super.show();
    }

    private VBox createLayout() {
        VBox layout = new VBox();
        tableView = new PropertiesTableView();
        ToolBar toolBar = new PropertyEditorToolBar(this);
        layout.getChildren().setAll(toolBar, tableView);
        layout.setMinWidth(750);
        return layout;
    }

    private void loadPropertiesIntoTable() {
        Properties properties = new Properties();
        ObservableList<PropertyWrapper> data = FXCollections.observableArrayList();
        try {
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            properties.entrySet().forEach(createPropertyWrappers(data));
            filteredProperties = new FilteredList<>(data, p -> true);
            tableView.getItems().setAll(new SortedList<>(filteredProperties));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Consumer<Map.Entry> createPropertyWrappers(ObservableList<PropertyWrapper> data) {
        return property -> data.add(new PropertyWrapper(property));
    }

    public void saveProperties() {
        try {
            StringBuilder builder = new StringBuilder();
            OutputStream outputStream = new FileOutputStream(file);
            tableView.getItems().forEach(property -> appendProperty(builder, property));
            outputStream.write(builder.toString().getBytes());
            setTitleWithoutAsterisk();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder appendProperty(StringBuilder builder, PropertyWrapper property) {
        return builder
                .append(property.getEscapedKey())
                .append("=")
                .append(property.getEscapedValue())
                .append(System.lineSeparator());
    }

    public void filter(String predicate) {
        filteredProperties.setPredicate(property -> {
            if (predicate.isEmpty()) {
                return true;
            }
            String lowerCasePredicate = predicate.toLowerCase();
            return property.getKey().toLowerCase().contains(lowerCasePredicate)
                    || property.getValue().toLowerCase().contains(lowerCasePredicate);
        });
        tableView.getItems().setAll(new SortedList<>(filteredProperties));
    }

    public void setTitleWithAsterisk() {
        super.setTitle(file.getAbsolutePath() + "*");
    }

    private void setTitleWithoutAsterisk() {
        super.setTitle(file.getAbsolutePath());
    }
}
