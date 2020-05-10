package pl.greywarden.openr;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.spring.MvvmfxSpringApplication;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.greywarden.openr.view.MainWindowView;

@SpringBootApplication
public class OpenR extends MvvmfxSpringApplication {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void startMvvmfx(Stage stage) {
        var viewTuple = FluentViewLoader.fxmlView(MainWindowView.class).load();
        var view = viewTuple.getView();
        var viewModel = viewTuple.getViewModel();

        stage.setScene(new Scene(view));
        stage.setMaximized(viewModel.isMaximized());
        stage.maximizedProperty().addListener(observable -> viewModel.isMaximizedProperty().setValue(((ReadOnlyBooleanProperty) observable).get()));
        stage.setTitle("OpenR");
        stage.show();
    }
}
