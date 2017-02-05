package pl.greywarden.openr.commons;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import pl.greywarden.openr.templates.Template;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class TemplateComboBox extends ComboBox<Template> {

    public TemplateComboBox() {
        super();
        setButtonCellAndCellFactory();
        super.getItems().setAll(Template.getAvailableTemplates());
        selectFirstItem();
        super.managedProperty().bind(itemCountBinding());
    }

    public TemplateComboBox(Template template) {
        super();
        setButtonCellAndCellFactory();
        super.getItems().setAll(template);
        selectFirstItem();
        super.managedProperty().bind(itemCountBinding());
    }

    private ListCell<Template> templateComboBoxListCell() {
        return new ListCell<Template>() {
            @Override
            protected void updateItem(Template t, boolean empty) {
                super.updateItem(t, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(getString(t.getName() + "-menu-item"));
                }
            }
        };
    }

    private void selectFirstItem() {
        super.getSelectionModel().select(0);
    }

    private void setButtonCellAndCellFactory() {
        super.setButtonCell(templateComboBoxListCell());
        super.setCellFactory(param -> templateComboBoxListCell());
    }

    private BooleanBinding itemCountBinding() {
        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return TemplateComboBox.super.getItems().size() > 1;
            }
        };
    }

    public Template getSelectedTemplate() {
        return super.getSelectionModel().getSelectedItem();
    }
}
