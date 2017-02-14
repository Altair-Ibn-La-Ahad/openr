package pl.greywarden.openr.gui.dialogs.grep;

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
        TableColumn<GrepResult, String> text = createNameColumn();
        TableColumn<GrepResult, String> pathToFile = createPathColumn();

        text.setCellValueFactory(this::matchTextStringProperty);
        pathToFile.setCellValueFactory(this::pathStringProperty);

        getColumns().setAll(text, pathToFile);
    }

    private SimpleStringProperty matchTextStringProperty(TableColumn.CellDataFeatures<GrepResult, String> param) {
        return new SimpleStringProperty(param.getValue().getText());
    }

    private SimpleStringProperty pathStringProperty(TableColumn.CellDataFeatures<GrepResult, String> param) {
        return new SimpleStringProperty(param.getValue().getFile().getAbsolutePath());
    }

    private TableColumn<GrepResult, String> createPathColumn() {
        return new TableColumn<>(getString("grep-result-path"));
    }

    private TableColumn<GrepResult, String> createNameColumn() {
        return new TableColumn<>(getString("grep-result-text"));
    }

}
