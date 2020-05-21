package pl.greywarden.openr.component.directoryview;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import pl.greywarden.openr.component.ButtonWithIcon;

public class DirectoryViewPathComponent extends GridPane {
    private final Label pathLabel = new Label();
    private final TextField pathTextField = new TextField();
    private final ButtonWithIcon goButton = new ButtonWithIcon("gmi-forward", 18);
    private final ButtonWithIcon refreshButton = new ButtonWithIcon("gmi-refresh", 18);

    public DirectoryViewPathComponent() {
        super();
        initComponent();
    }

    private void initComponent() {
        this.pathLabel.setText("Path");
        final var labelColumnConstraint = new ColumnConstraints();
        final var pathColumnConstraint = new ColumnConstraints();
        final var goButtonColumnConstraint = new ColumnConstraints();
        final var refreshButtonColumnConstraint = new ColumnConstraints();

        pathColumnConstraint.setFillWidth(true);
        pathColumnConstraint.setHgrow(Priority.ALWAYS);

        super.setHgap(5.0);
        super.getColumnConstraints().setAll(labelColumnConstraint, pathColumnConstraint, goButtonColumnConstraint, refreshButtonColumnConstraint);

        super.addRow(0, pathLabel, pathTextField, goButton, refreshButton);
    }

    public void setChangePathHandler(EventHandler<ActionEvent> handler) {
        pathTextField.setOnAction(handler);
        goButton.setOnAction(handler);
    }

    public StringProperty textProperty() {
        return this.pathTextField.textProperty();
    }
}
