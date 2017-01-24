package pl.greywarden.openr.gui.directoryview;

import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryViewDataBuilder {

    private final List<File> files;
    private final DirectoryEntry rootEntry;

    public DirectoryViewDataBuilder(DirectoryEntry rootEntry) {
        this.rootEntry = rootEntry;
        File rootDirectory = new File(rootEntry.getEntryProperties().getAbsolutePath());
        File[] filesArray = rootDirectory.listFiles();
        this.files = Arrays.asList(filesArray == null ? FileUtils.EMPTY_FILE_ARRAY : filesArray)
                .parallelStream()
                .filter(file -> !file.isHidden())
                .collect(Collectors.toList());
    }

    private List<EntryWrapper> createEntryWrappers() {
        List<AbstractEntry> abstractEntries = Collections.synchronizedList(new ArrayList<>());
        List<File> synchronizedFiles = Collections.synchronizedList(files);
        synchronizedFiles.parallelStream().forEach(file -> abstractEntries.add(file.isDirectory()
                ? new DirectoryEntry(file.getAbsolutePath())
                : new FileEntry(file.getAbsolutePath())));

        List<EntryWrapper> result = Collections.synchronizedList(new ArrayList<>());
        if (rootEntry.getEntryProperties().hasParent()) {
            result.add(createParentDirectoryEntryWrapper());
        }
        abstractEntries.parallelStream().forEach(entry -> result.add(new EntryWrapper(entry)));
        return result;
    }

    private ParentDirectoryEntry createParentDirectoryEntry() {
        String parentDirectoryPath = rootEntry.getEntryProperties().getParentFile().getAbsolutePath();
        return new ParentDirectoryEntry(parentDirectoryPath);
    }

    private EntryWrapper createParentDirectoryEntryWrapper() {
        return new EntryWrapper(createParentDirectoryEntry());
    }

    public List<EntryWrapper> getData() {
        return Collections.synchronizedList(createEntryWrappers());
    }

}
