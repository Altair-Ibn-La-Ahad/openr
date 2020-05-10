package pl.greywarden.openr.view;

import de.saxsys.mvvmfx.ViewModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/app-info.properties")
public class MainWindowViewModel implements ViewModel {
    @Value("${app-version}")
    private String applicationVersion;

    String getApplicationVersion() {
        return applicationVersion;
    }
}
