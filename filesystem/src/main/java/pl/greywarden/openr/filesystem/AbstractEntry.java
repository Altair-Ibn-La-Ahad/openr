package pl.greywarden.openr.filesystem;

import javafx.beans.binding.BooleanBinding;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@Log4j
public abstract class AbstractEntry implements EntryOperations {

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

    @Override
    public void paste(AbstractEntry target) {
        try {
            System.err.println("PASTE");
            System.err.println(clipboard.getEntryProperties().getAbsolutePath());
            if (clipboard.getEntryProperties().isDirectory()) {
                FileUtils.copyDirectoryToDirectory(clipboard.filesystemEntry, target.filesystemEntry);
            } else {
                FileUtils.copyFileToDirectory(clipboard.filesystemEntry, target.filesystemEntry);
            }
            clipboard = null;
        } catch (IOException exception) {
            log.error("Exception during paste", exception);
        }
    }

    @Override
    public void copy() {
        clipboard = this;
        System.err.println(clipboard.getEntryProperties().getAbsolutePath());
        cut = false;
    }

    @Override
    public void cut() {
        copy();
        System.err.println(clipboard.getEntryProperties().getAbsolutePath());
        //cut = true;
    }

    public static BooleanBinding clipboardEmptyBinding() {
        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return clipboard == null;
            }
        };
    }

}
