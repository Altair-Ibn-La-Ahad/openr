package pl.greywarden.openr.view;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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

    public void showAboutDialog() {
        var appVersion = mainWindowViewModel.getApplicationVersion();
        var content = new Label(String.format("OpenR file manager, version: %s%n%nAuthor: Marcin Las%nEmail: las.marcin.94@gmail.com", appVersion));
        var aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("About");
        aboutDialog.setHeaderText("OpenR");
        aboutDialog.getDialogPane().setContent(content);
        
        aboutDialog.show();
    }
}
