package pl.greywarden.openr.view;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainWindowView implements FxmlView<MainWindowViewModel> {
    @InjectViewModel
    private MainWindowViewModel mainWindowViewModel;

}
