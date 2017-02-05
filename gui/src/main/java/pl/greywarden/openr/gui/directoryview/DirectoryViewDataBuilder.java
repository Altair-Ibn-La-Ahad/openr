package pl.greywarden.openr.gui.directoryview;

import org.apache.commons.io.FileUtils;
import pl.greywarden.openr.filesystem.AbstractEntry;
import pl.greywarden.openr.filesystem.DirectoryEntry;
import pl.greywarden.openr.filesystem.EntryWrapper;
import pl.greywarden.openr.filesystem.FileEntry;
import pl.greywarden.openr.filesystem.ParentDirectoryEntry;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryViewDataBuilder {

    private final List<File> files;
    private final DirectoryEntry rootEntry;

    public DirectoryViewDataBuilder(DirectoryEntry rootEntry) {
        this.rootEntry = rootEntry;
        this.files = getFiles();
    }

    private List<File> getFiles() {
        File[] filesArray = rootEntry.getFilesystemEntry().listFiles();
        List<File> files = Arrays.asList(filesArray == null ? FileUtils.EMPTY_FILE_ARRAY : filesArray);
        Stream<File> stream = files.size() > 10 ? files.parallelStream() : files.stream();
        return stream.filter(file -> !file.isHidden()).collect(Collectors.toList());
    }

    private List<EntryWrapper> createEntryWrappers() {
        List<AbstractEntry> abstractEntries = Collections.synchronizedList(new LinkedList<>());
        List<File> synchronizedFiles = Collections.synchronizedList(files);
        List<EntryWrapper> result = Collections.synchronizedList(new LinkedList<>());

        createAbstractEntries(abstractEntries, synchronizedFiles);
        createParentDirectoryWrapper(result);
        convertEntriesToWrappers(abstractEntries, result);
        sortWrappers(result);

        return result;
    }

    private void convertEntriesToWrappers(List<AbstractEntry> abstractEntries, List<EntryWrapper> result) {
        Stream<AbstractEntry> stream = abstractEntries.size() > 10
                ? abstractEntries.parallelStream()
                : abstractEntries.stream();
        stream.forEach(entry -> result.add(new EntryWrapper(entry)));
    }

    private void createParentDirectoryWrapper(List<EntryWrapper> result) {
        if (rootEntry.getEntryProperties().hasParent()) {
            result.add(createParentDirectoryEntryWrapper());
        }
    }

    private void createAbstractEntries(List<AbstractEntry> abstractEntries, List<File> synchronizedFiles) {
        Stream<File> stream = synchronizedFiles.size() > 10
                ? synchronizedFiles.parallelStream()
                : synchronizedFiles.stream();
        stream.forEachOrdered(file -> abstractEntries.add(file.isDirectory()
                        ? new DirectoryEntry(file.getAbsolutePath())
                        : new FileEntry(file.getAbsolutePath())));
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

    private void sortWrappers(List<EntryWrapper> wrappers) {
        wrappers.sort((o1, o2) -> {
            AbstractEntry a1 = o1.getEntry();
            AbstractEntry a2 = o2.getEntry();
            if ("..".equals(a1.getEntryProperties().getBaseName())) {
                return -1;
            }
            if ("..".equals(a2.getEntryProperties().getBaseName())) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        });
    }
}
