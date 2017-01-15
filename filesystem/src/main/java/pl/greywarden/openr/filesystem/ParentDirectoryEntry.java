package pl.greywarden.openr.filesystem;

import java.io.File;

public class ParentDirectoryEntry extends DirectoryEntry {

    private String path;

    public ParentDirectoryEntry(String path) {
        super(path);
        this.path = path;
    }

    @Override
    public EntryProperties getEntryProperties() {
        File directory = new File(this.path);
        return new EntryProperties(directory) {
            @Override
            public String getBaseName() {
                return "..";
            }
        };
    }
}
