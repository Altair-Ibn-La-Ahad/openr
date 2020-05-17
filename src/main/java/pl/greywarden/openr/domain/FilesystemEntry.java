package pl.greywarden.openr.domain;

import lombok.Data;

import java.net.URI;

@Data
public class FilesystemEntry {
    private final URI uri;
    private final EntryType type;
    private final boolean hidden;

    public enum EntryType {
        FILE,
        DIRECTORY
    }
}
