package pl.greywarden.openr.view;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pl.greywarden.openr.domain.DirectoryContent;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;
import pl.greywarden.openr.service.FilesystemEntryWrapperFactory;
import pl.greywarden.openr.service.FilesystemService;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:/app-info.properties")
public class MainWindowViewModel implements ViewModel {
    private final Preferences applicationSettings;
    private final FilesystemService filesystemService;

    private final StringProperty selectedFileProperty = new SimpleStringProperty();

    private final StringProperty leftPath = new SimpleStringProperty();
    private final StringProperty rightPathProperty = new SimpleStringProperty();

    private final DoubleProperty mainWindowWidthProperty = new SimpleDoubleProperty();
    private final DoubleProperty mainWindowHeightProperty = new SimpleDoubleProperty();
    private final BooleanProperty isMaximizedProperty = new SimpleBooleanProperty();

    private final BooleanProperty showHiddenFilesProperty = new SimpleBooleanProperty(false);

    private final FilesystemEntryWrapperFactory filesystemEntryWrapperFactory;

    public MainWindowViewModel(Preferences applicationSettings,
                               FilesystemService filesystemService,
                               FilesystemEntryWrapperFactory filesystemEntryWrapperFactory) {
        this.applicationSettings = applicationSettings;

        mainWindowWidthProperty.setValue(applicationSettings.getDouble("main-window.width", 1024));
        mainWindowHeightProperty.setValue(applicationSettings.getDouble("main-window.height", 768));
        isMaximizedProperty.setValue(applicationSettings.getBoolean("main-window.is-maximized", true));

        leftPath.setValue(applicationSettings.get("main-window.left-path", System.getProperty("user.dir")));
        rightPathProperty.setValue(applicationSettings.get("main-window.right-path", System.getProperty("user.dir")));

        this.filesystemService = filesystemService;
        this.filesystemEntryWrapperFactory = filesystemEntryWrapperFactory;
    }

    @Value("${app-version}")
    private String applicationVersion;

    @PreDestroy
    public void storeChanges() {
        applicationSettings.putDouble("main-window.width", mainWindowWidthProperty.getValue());
        applicationSettings.putDouble("main-window.height", mainWindowHeightProperty.getValue());
        applicationSettings.putBoolean("main-window.is-maximized", isMaximizedProperty.getValue());
    }

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

    public boolean isMaximized() {
        return isMaximizedProperty.getValue();
    }

    public double getWidth() {
        return mainWindowWidthProperty.getValue();
    }

    public double getHeight() {
        return mainWindowHeightProperty.getValue();
    }

    public DoubleProperty mainWindowWidthProperty() {
        return this.mainWindowWidthProperty;
    }

    public DoubleProperty mainWindowHeightProperty() {
        return this.mainWindowHeightProperty;
    }

    public BooleanProperty isMaximizedProperty() {
        return this.isMaximizedProperty;
    }

    public StringProperty rightPathProperty() {
        return rightPathProperty;
    }

    public StringProperty leftPathProperty() {
        return leftPath;
    }

    public BooleanProperty showHiddenFilesProperty() {
        return showHiddenFilesProperty;
    }

    public DirectoryContent getFiles(String path) {
        var directory = new File(path);
        var directoryContent = new DirectoryContent(
                filesystemService.getContentOfDirectory(directory)
                        .stream()
                        .filter(entry -> showHiddenFilesProperty.getValue() || !entry.isHidden())
                        .map(filesystemEntryWrapperFactory::wrap)
                        .collect(Collectors.toList()));
        if (directory.getParentFile() != null) {
            directoryContent.add(createParentDirectoryEntry());
            directoryContent.setHasParent(true);
        }
        return directoryContent;
    }

    private FilesystemEntryWrapper createParentDirectoryEntry() {
        return new FilesystemEntryWrapper();
    }
}
