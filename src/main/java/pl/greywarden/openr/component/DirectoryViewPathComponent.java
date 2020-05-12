package pl.greywarden.openr.component;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class DirectoryViewPathComponent extends GridPane {
    private final StringProperty directoryViewId = new SimpleStringProperty();
    private final Label pathLabel = new Label();
    private final TextField pathTextField = new TextField();
    private final ButtonWithIcon goButton = new ButtonWithIcon("gmi-forward", 18);
    private final ButtonWithIcon refreshButton = new ButtonWithIcon("gmi-refresh", 18);

    public DirectoryViewPathComponent(@NamedArg("directoryViewId") String directoryViewId) {
        super();
        this.directoryViewId.setValue(directoryViewId);
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

    public StringProperty textProperty() {
        return this.pathTextField.textProperty();
    }
}
