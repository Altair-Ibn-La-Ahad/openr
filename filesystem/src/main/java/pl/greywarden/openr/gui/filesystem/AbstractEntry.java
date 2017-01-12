package pl.greywarden.openr.gui.filesystem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public abstract class AbstractEntry {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private File filesystemEntry;
    private EntryProperties entryProperties;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected static AbstractEntry clipboard;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected boolean cut;

    protected AbstractEntry(String path) {
        filesystemEntry = new File(path);
        entryProperties = new EntryProperties(filesystemEntry);
    }

    public boolean isClipboardEmpty() {
        return clipboard == null;
    }

}
