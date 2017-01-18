package pl.greywarden.openr.gui.scenes;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;
import pl.greywarden.openr.gui.wrappers.DirectoryViewWrapper;

public class CentralContainter extends HBox {

    @Getter
    private final DirectoryViewWrapper leftView;
    @Getter
    private final DirectoryViewWrapper rightView;

    public CentralContainter() {
        leftView = new DirectoryViewWrapper(System.getProperty("user.dir"));
        rightView = new DirectoryViewWrapper(System.getProperty("user.dir"));

        leftView.maxWidthProperty().bind(super.widthProperty().multiply(0.5));
        rightView.maxWidthProperty().bind(super.widthProperty().multiply(0.5));

        HBox.setHgrow(leftView, Priority.ALWAYS);
        HBox.setHgrow(rightView, Priority.ALWAYS);
        String delimiter = "-fx-border-color: gray;\n" +
                "-fx-border-width: 0 2 0 0;\n";
        leftView.setStyle(delimiter);
        super.getChildren().addAll(leftView, rightView);
        leftView.getDirectoryView().setId("left-view");
        rightView.getDirectoryView().setId("right-view");
    }

    public String getLeftViewRootPath() {
        return leftView.getRoot();
    }

    public String getRightViewRootPath() {
        return rightView.getRoot();
    }

}
