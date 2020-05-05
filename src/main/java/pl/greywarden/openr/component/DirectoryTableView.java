package pl.greywarden.openr.component;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DirectoryTableView extends TableView {
    private final TableColumn nameColumn = new TableColumn();
    private final TableColumn extensionColumn = new TableColumn();
    private final TableColumn sizeColumn = new TableColumn();
    private final TableColumn modifiedColumn = new TableColumn();
    private final TableColumn privilegesColumn = new TableColumn();

    public DirectoryTableView() {
        super();
        initComponent();
    }

    private void initComponent() {
        createColumns();
        super.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        super.setPlaceholder(new Label());
    }

    private void createColumns() {
        super.getColumns().setAll(nameColumn, extensionColumn, sizeColumn, modifiedColumn, privilegesColumn);
        nameColumn.setText("Name");
        extensionColumn.setText("Ext.");
        sizeColumn.setText("Size");
        modifiedColumn.setText("Modified");
        privilegesColumn.setText("Priv.");
    }
}
