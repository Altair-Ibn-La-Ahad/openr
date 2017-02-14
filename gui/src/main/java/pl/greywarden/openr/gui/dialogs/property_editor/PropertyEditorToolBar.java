package pl.greywarden.openr.gui.dialogs.property_editor;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.dialogs.property_editor.table_view.PropertyWrapper;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class PropertyEditorToolBar extends ToolBar {

    private final PropertyEditorDialog parent;

    public PropertyEditorToolBar(PropertyEditorDialog parent) {
        super();
        this.parent = parent;
        createToolBar();
    }

    private void createToolBar() {
        Button save = createSaveButton();
        Button add = createAddButton();
        Button remove = createRemoveButton();
        Button exit = createExitButton();
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField filter = createFilterInput();

        super.getItems().addAll(save, new Separator(),
                add, remove, new Separator(),
                exit, spacer, filter);
    }

    private TextField createFilterInput() {
        TextField filter = new TextField();
        filter.textProperty().addListener((observable, oldValue, newValue) -> parent.filter(newValue));
        filter.setOnKeyPressed(event -> {
            if (KeyCode.ESCAPE.equals(event.getCode())) {
                filter.setText("");
            }
        });
        return filter;
    }

    private Button createExitButton() {
        Button exit = new Button();
        exit.setGraphic(IconManager.getProgramIcon("exit"));
        exit.setOnAction(event -> parent.close());
        return exit;
    }

    private Button createRemoveButton() {
        Button remove = new Button();
        remove.setGraphic(IconManager.getProgramIcon("minus"));
        remove.setOnAction(event -> handleRemove());
        return remove;
    }

    private void handleRemove() {
        PropertyWrapper selected = parent.getTableView().getSelectionModel().getSelectedItem();
        parent.getTableView().getItems().remove(selected);
        parent.getTableView().editedProperty.setValue(true);
        parent.setTitleWithoutAsterisk();
        parent.propertiesEdited.invalidate();
    }

    private Button createAddButton() {
        Button add = new Button();
        add.setGraphic(IconManager.getProgramIcon("plus"));
        add.setOnAction(event -> new NewPropertyDialog(parent));
        return add;
    }

    private Button createSaveButton() {
        Button save = new Button();
        save.setGraphic(IconManager.getProgramIcon("save"));
        save.tooltipProperty().setValue(new Tooltip(getString("save")));
        save.setOnAction(event -> handleSaveAction());
        save.disableProperty().bind(parent.propertiesEdited.not());
        return save;
    }

    private void handleSaveAction() {
        parent.saveProperties();
        parent.getTableView().editedProperty.set(false);
        parent.propertiesEdited.invalidate();
    }
}
