package pl.greywarden.openr.commons;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.main_window.MainWindow;

import java.io.File;

public class PathComboBox extends ComboBox<DirectoryView> {

    public PathComboBox() {
        super();
        if (leftViewVisible()) {
            if (rightViewVisible()) {
                addBothViews();
            } else {
                addLeftView();
            }
        }
        if (rightViewVisible() && !leftViewVisible()) {
            addRightView();
        }
        createComponent();
        super.getEditor().setOnKeyPressed(this::handleKeyEvent);
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.isControlDown() && KeyCode.TAB.equals(event.getCode())) {
            selectNextItem();
            super.getEditor().positionCaret(getSelectedPath().length());
            event.consume();
        }
    }

    private void selectNextItem() {
        int curentIndex = super.getSelectionModel().getSelectedIndex();
        int maxIndex = super.getItems().size();
        if (curentIndex == maxIndex - 1) {
            super.getSelectionModel().select(0);
        } else {
            super.getSelectionModel().select(maxIndex - 1);
        }
    }

    private boolean rightViewVisible() {
        return MainWindow.getRightWrapper().isVisible();
    }

    private boolean leftViewVisible() {
        return MainWindow.getLeftWrapper().isVisible();
    }

    private void addRightView() {
        super.getItems().setAll(MainWindow.getRightDirectoryView());
    }

    private void addLeftView() {
        super.getItems().setAll(MainWindow.getLeftDirectoryView());
    }

    private void addBothViews() {
        super.getItems().setAll(MainWindow.getLeftDirectoryView(), MainWindow.getRightDirectoryView());
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
                if (isLeftRootPath(string)) {
                    return MainWindow.getLeftDirectoryView();
                }
                if (isRightRootPath(string)) {
                    return MainWindow.getRightDirectoryView();
                }
                return new DirectoryView(string);
            }

            private boolean isRightRootPath(String string) {
                return string.equals(MainWindow.getRightDirectoryView().getRootPath());
            }

            private boolean isLeftRootPath(String string) {
                return string.equals(MainWindow.getLeftDirectoryView().getRootPath());
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
        if (isSelectionDirectoryView(path)) {
            super.getSelectionModel().getSelectedItem().reload();
        }
    }

    private boolean isSelectionDirectoryView(String path) {
        return path.equals(MainWindow.getLeftDirectoryView().getRootPath())
                || path.equals(MainWindow.getRightDirectoryView().getRootPath());
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
