package pl.greywarden.openr.gui.grep;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class GrepResultTableView extends TableView<GrepResult> {

    public GrepResultTableView() {
        super();
        super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        super.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        super.setPlaceholder(new Label(""));

        createColumns();

    }

    @SuppressWarnings("unchecked")
    private void createColumns() {
        TableColumn<GrepResult, String> text = new TableColumn<>(getString("grep-result-text"));
        TableColumn<GrepResult, String> pathToFile = new TableColumn<>(getString("grep-result-path"));

        text.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getText()));
        pathToFile.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFile().getAbsolutePath()));

        super.getColumns().setAll(text, pathToFile);
    }

}
