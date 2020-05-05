package pl.greywarden.openr;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.spring.MvvmfxSpringApplication;
import javafx.application.Application;
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
        var view = FluentViewLoader.fxmlView(MainWindowView.class).load().getView();
        stage.setScene(new Scene(view));
        stage.setTitle("OpenR");
        stage.show();
    }
}
