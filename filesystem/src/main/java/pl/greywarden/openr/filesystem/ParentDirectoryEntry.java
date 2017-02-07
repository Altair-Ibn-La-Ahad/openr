package pl.greywarden.openr.filesystem;

import java.io.File;

public class ParentDirectoryEntry extends DirectoryEntry {

    private final String path;

    public ParentDirectoryEntry(String path) {
        super(path);
        this.path = path;
    }

    @Override
    @SuppressWarnings("unused")
    public EntryProperties getEntryProperties() {
        File directory = new File(this.path);
        return new EntryProperties(directory) {
            @SuppressWarnings("unused")
            @Override
            public String getBaseName() {
                return "..";
            }
        };
    }
}
