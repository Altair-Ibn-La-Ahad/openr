package pl.greywarden.openr.commons;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import pl.greywarden.openr.gui.directoryview.DirectoryView;
import pl.greywarden.openr.gui.scenes.main_window.MainWindow;

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
        selectFirstItem();
        super.managedProperty().bind(itemCountBinding());
        setButtonCellAndCellFactory();
    }

    public PathComboBox(DirectoryView selectedView) {
        super();
        super.getItems().setAll(selectedView);
        selectFirstItem();
        super.managedProperty().bind(itemCountBinding());
        setButtonCellAndCellFactory();
    }

    public String getSelectedPath() {
        return super.getSelectionModel().getSelectedItem().getRootPath();
    }

    private void selectFirstItem() {
        super.getSelectionModel().select(0);
    }

    private BooleanBinding itemCountBinding() {
        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return PathComboBox.super.getItems().size() > 1;
            }
        };
    }

    private static ListCell<DirectoryView> pathComboBoxButtonCell() {
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
        super.getSelectionModel().getSelectedItem().reload();
    }
}
