package pl.greywarden.openr.commons;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

import java.io.File;

public class PathComboBox extends ComboBox<DirectoryView> {

    public PathComboBox() {
        super();
        if (MainWindow.getLeftWrapper().isVisible()) {
            if (MainWindow.getRightWrapper().isVisible()) {
                super.getItems().setAll(MainWindow.getLeftDirectoryView(), MainWindow.getRightDirectoryView());
            } else {
                super.getItems().setAll(MainWindow.getLeftDirectoryView());
            }
        }
        if (MainWindow.getRightWrapper().isVisible()) {
            if (!MainWindow.getLeftWrapper().isVisible()) {
                super.getItems().setAll(MainWindow.getRightDirectoryView());
            }
        }
        createComponent();
    }

    private void createComponent() {
        setButtonCellAndCellFactory();
        super.setConverter(new StringConverter<DirectoryView>() {
            @Override
            public String toString(DirectoryView object) {
                return object == null ? "" : object.getRootPath();
            }

            @Override
            public DirectoryView fromString(String string) {
                if (string.equals(MainWindow.getLeftDirectoryView().getRootPath())) {
                    return MainWindow.getLeftDirectoryView();
                }
                if (string.equals(MainWindow.getRightDirectoryView().getRootPath())) {
                    return MainWindow.getRightDirectoryView();
                }
                return new DirectoryView(string);
            }
        });
        super.setEditable(true);
        super.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            File enteredDirectoryPath = new File(newValue);
            if (enteredDirectoryPath.exists() && enteredDirectoryPath.isDirectory()) {
                super.getEditor().setStyle(null);
                pathValidBinding.invalidate();
            } else {
                super.getEditor().setStyle("-fx-text-fill: red");
                pathValidBinding.invalidate();
            }
        });
        selectFirstItem();
    }

    public PathComboBox(DirectoryView selectedView) {
        super();
        super.getItems().setAll(selectedView);
        createComponent();
        super.managedProperty().setValue(false);
    }

    public String getSelectedPath() {
        return super.getSelectionModel().getSelectedItem().getRootPath();
    }

    private void selectFirstItem() {
        super.getSelectionModel().select(0);
    }

    private ListCell<DirectoryView> pathComboBoxButtonCell() {
        return new ListCell<DirectoryView>() {
            @Override
            protected void updateItem(DirectoryView dv, boolean empty) {
                super.updateItem(dv, empty);
                if (!empty) {
                    setText(dv.getRootPath());
                }
            }
        };
    }

    private void setButtonCellAndCellFactory() {
        super.setButtonCell(pathComboBoxButtonCell());
        super.setCellFactory(param -> pathComboBoxButtonCell());
    }

    public void reloadSelected() {
        String path = getSelectedPath();
        if (path.equals(MainWindow.getLeftDirectoryView().getRootPath())
                || path.equals(MainWindow.getRightDirectoryView().getRootPath())) {
            super.getSelectionModel().getSelectedItem().reload();
        }
    }

    public final BooleanBinding pathValidBinding = Bindings.createBooleanBinding(() -> {
        String path = PathComboBox.super.getEditor().textProperty().get();
        if (path.isEmpty()) {
            path = getSelectedPath();
        }
        File file = new File(path);
        return !(file.exists() && file.isDirectory());
    });
}