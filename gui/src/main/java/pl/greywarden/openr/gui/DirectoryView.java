package pl.greywarden.openr.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.i18n.I18nManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DirectoryView extends TableView <AbstractEntry> {

    private String path;
    private DirectoryEntry root;

    private final I18nManager i18n = I18nManager.getInstance();

    public DirectoryView(String path) {
        this.path = path;
        this.root = new DirectoryEntry(path);
        build();
    }

    public void changePath(String path) {
        this.path = path;
        root = new DirectoryEntry(path);
        build();
    }

    @SuppressWarnings("unchecked")
    private void build() {
        File rootDirectory = new File(root.getEntryProperties().getAbsolutePath());
        File[] filesArray = rootDirectory.listFiles();
        if (filesArray == null) {
            filesArray = FileUtils.EMPTY_FILE_ARRAY;
        }
        Collection<File> files = Arrays.asList(filesArray);
        Collections.sort((List<File>)files, (first, second) -> first.getName().compareTo(second.getName()));

        i18n.setBundle("directory-view");

        TableColumn <AbstractEntry, String>
                name = new TableColumn<>(i18n.getString("name")),
                extension = new TableColumn<>(i18n.getString("extension")),
                size = new TableColumn<>(i18n.getString("size")),
                modificationDate = new TableColumn<>(i18n.getString("modification-date")),
                privileges = new TableColumn<>(i18n.getString("privileges"));

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        extension.setCellValueFactory(new PropertyValueFactory<>("extension"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        modificationDate.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        privileges.setCellValueFactory(new PropertyValueFactory<>("privileges"));
        super.getColumns().add(name);
        super.getColumns().add(extension);
        super.getColumns().add(size);
        super.getColumns().add(modificationDate);
        super.getColumns().add(privileges);
        final ObservableList data = FXCollections.observableList(createEntries(files));
        super.setItems(data);
    }

    private List<EntryWrapper> createEntries(Collection<File> files) {
        List<AbstractEntry> abstractEntries = new ArrayList<>();
        files.forEach(file -> abstractEntries.add(file.isDirectory()
                ? new DirectoryEntry(file.getAbsolutePath())
                : new FileEntry(file.getAbsolutePath())));

        List<EntryWrapper> result = new ArrayList<>();
        abstractEntries.forEach(entry -> result.add(new EntryWrapper(entry)));
        return result;
    }

    @Getter
    @Setter
    public static class EntryWrapper {
        private final String name;
        private final String extension;
        private final String size;
        private final String modificationDate;
        private final String privileges;

        private EntryWrapper(AbstractEntry entry) {
            this.name = entry.getEntryProperties().getName();
            this.extension = entry.getEntryProperties().getExtension();
            this.size = FileUtils.byteCountToDisplaySize(entry.getEntryProperties().getSizeInBytes());
            this.privileges = SystemUtils.IS_OS_WINDOWS
                    ? entry.getEntryProperties().getDosFilePermissions()
                    : entry.getEntryProperties().getPosixFilePermissions();
            Date date = new Date();
            date.setTime(entry.getEntryProperties().getLastModified());
            this.modificationDate = new SimpleDateFormat("HH:mm:ss YYYY-MM-dd").format(date);
        }
    }
}
