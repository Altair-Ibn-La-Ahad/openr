package pl.greywarden.openr.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryViewDataBuilder {

    private Collection<File> files;
    private DirectoryEntry rootEntry;

    public DirectoryViewDataBuilder(DirectoryEntry rootEntry) {
        this.rootEntry = rootEntry;
        File rootDirectory = new File(rootEntry.getEntryProperties().getAbsolutePath());
        File[] filesArray = rootDirectory.listFiles();
        this.files = Arrays.stream(filesArray == null ? FileUtils.EMPTY_FILE_ARRAY : filesArray)
                .filter(file -> !file.isHidden())
                .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .collect(Collectors.toList());
    }

    private List<EntryWrapper> createEntryWrappers() {
        List<AbstractEntry> abstractEntries = new ArrayList<>();
        files.forEach(file -> abstractEntries.add(file.isDirectory()
                ? new DirectoryEntry(file.getAbsolutePath())
                : new FileEntry(file.getAbsolutePath())));

        List<EntryWrapper> result = new ArrayList<>();
        if (rootEntry.getEntryProperties().hasParent()) {
            result.add(createParentDirectoryEntryWrapper());
        }
        abstractEntries.forEach(entry -> result.add(new EntryWrapper(entry)));
        return result;
    }

    private ParentDirectoryEntry createParentDirectoryEntry() {
        String parentDirectoryPath = rootEntry.getEntryProperties().getParentFile().getAbsolutePath();
        return new ParentDirectoryEntry(parentDirectoryPath);
    }

    private EntryWrapper createParentDirectoryEntryWrapper() {
        return new EntryWrapper(createParentDirectoryEntry());
    }

    public ObservableList getData() {
        return FXCollections.observableList(createEntryWrappers());
    }

}
