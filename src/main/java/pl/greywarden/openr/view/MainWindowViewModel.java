package pl.greywarden.openr.view;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/app-info.properties")
class MainWindowViewModel implements ViewModel {
    private final StringProperty selectedFileProperty = new SimpleStringProperty();

    @Value("${app-version}")
    private String applicationVersion;

    String getApplicationVersion() {
        return applicationVersion;
    }

    StringProperty selectedFileProperty() {
        return selectedFileProperty;
    }

    String getSelectedFile() {
        return selectedFileProperty.getValue();
    }

    void setSelectedFile(String selectedFile) {
        this.selectedFileProperty.setValue(selectedFile);
    }

}
