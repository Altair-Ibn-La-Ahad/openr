package pl.greywarden.openr.view;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainWindowView implements FxmlView<MainWindowViewModel> {
    @InjectViewModel
    private MainWindowViewModel mainWindowViewModel;

    public void exitApplication() {
        var confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        confirmationDialog.setTitle("Confirm exit");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to exit?");
        confirmationDialog
                .showAndWait()
                .filter(ButtonType.YES::equals)
                .ifPresent(b -> Platform.runLater(Platform::exit));
    }
}
