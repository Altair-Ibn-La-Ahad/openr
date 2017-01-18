package pl.greywarden.openr.gui.wrappers;

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
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.gui.directoryview.PathTextField;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.i18n.I18nManager;

public class DirectoryViewWrapper extends VBox {

    @Getter
    private final DirectoryView directoryView;

    @SuppressWarnings("unchecked")
    public DirectoryViewWrapper(String pathToRoot) {
        super();
        I18nManager i18n = I18nManager.getInstance();
        i18n.setBundle("directory-view");

        directoryView = new DirectoryView(pathToRoot);
        PathTextField pathTextField = new PathTextField(directoryView);
        HBox pathTextFieldWrapper = new HBox();
        Label description = new Label(i18n.getString("path"));
        Button goButton = new Button("->");
        BorderPane borderPane = new BorderPane(directoryView);

        description.setPadding(new Insets(0, 5, 0, 5));
        borderPane.setPadding(new Insets(0, 5, 5, 5));
        pathTextFieldWrapper.setPadding(new Insets(5, 5, 5, 5));

        goButton.setOnMouseClicked(event -> pathTextField.goToEnteredDirectory());

        HBox.setHgrow(pathTextField, Priority.ALWAYS);
        HBox.setMargin(goButton, new Insets(0, 5, 0, 5));

        pathTextFieldWrapper.setAlignment(Pos.CENTER);
        pathTextFieldWrapper.getChildren().addAll(description, pathTextField, goButton);

        VBox.setVgrow(borderPane, Priority.ALWAYS);
        super.getChildren().addAll(pathTextFieldWrapper, borderPane);

        directoryView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            EntryWrapper wrapper = (EntryWrapper) newValue;
            TextField path = (TextField) super.getParent().getScene().lookup("#statusbar-text-field");
            if (wrapper != null) {
                path.setText(wrapper.getEntry().getEntryProperties().getAbsolutePath());
            } else {
                path.setText("");
            }
        });
    }

    public String getRoot() {
        return directoryView.getRootPath();
    }

}
