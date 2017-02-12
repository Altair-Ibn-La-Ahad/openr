package pl.greywarden.openr.gui.directoryview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class DirectoryViewWrapper extends VBox {

    @Getter
    private final DirectoryView directoryView;

    public DirectoryViewWrapper(String pathToRoot) {
        super();

        directoryView = new DirectoryView(pathToRoot);
        BorderPane borderPane = new BorderPane(directoryView);

        borderPane.setPadding(new Insets(0, 5, 5, 5));

        VBox.setVgrow(borderPane, Priority.ALWAYS);
        createPathTextFieldWrapper();

        super.getChildren().addAll(borderPane);

        directoryView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    TextField path = MainWindow.getStatusBar().getPathTextField();
                    if (newValue != null) {
                        path.setText(newValue.getEntry().getEntryProperties().getAbsolutePath());
                    } else {
                        path.setText("");
                    }
                });
        super.visibleProperty().bind(managedProperty());
    }

    private void createPathTextFieldWrapper() {
        HBox pathTextFieldWrapper = new HBox(5);

        DirectoryViewPathTextField pathTextField = new DirectoryViewPathTextField(directoryView);
        Label pathTextFieldLabel = new Label(getString("path"));
        Button go = new Button();
        Button refresh = new Button();

        go.setGraphic(IconManager.getProgramIcon("go"));
        refresh.setGraphic(IconManager.getProgramIcon("refresh"));

        go.setOnAction(e -> pathTextField.goToEnteredDirectory());
        refresh.setOnAction(e -> directoryView.reload());

        pathTextFieldWrapper.getChildren().addAll(pathTextFieldLabel, pathTextField, go, refresh);
        HBox.setHgrow(pathTextField, Priority.ALWAYS);
        pathTextFieldWrapper.setPadding(new Insets(3, 5, 3, 5));
        pathTextFieldWrapper.setAlignment(Pos.BASELINE_LEFT);

        super.getChildren().add(pathTextFieldWrapper);
    }

}
