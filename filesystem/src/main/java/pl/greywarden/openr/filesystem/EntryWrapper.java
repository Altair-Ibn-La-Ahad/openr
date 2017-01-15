package pl.greywarden.openr.filesystem;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.util.Date;

@Getter
public class EntryWrapper {

    private final String name;
    private final String extension;
    private final Long size;
    private final Date modificationDate;
    private final String privileges;
    private final AbstractEntry entry;

    public EntryWrapper(AbstractEntry entry) {
        this.entry = entry;
        this.name = entry.getEntryProperties().isDirectory()
                ? entry.getEntryProperties().getName()
                : FilenameUtils.getBaseName(entry.getEntryProperties().getName());
        this.extension = entry instanceof ParentDirectoryEntry
                ? ".." : entry.getEntryProperties().getExtension();
        this.size = entry instanceof ParentDirectoryEntry
                ? Long.MIN_VALUE : entry.getEntryProperties().getSizeInBytes();
        this.privileges =entry instanceof ParentDirectoryEntry
                ? ".." : SystemUtils.IS_OS_WINDOWS
                ? entry.getEntryProperties().getDosFilePermissions()
                : entry.getEntryProperties().getPosixFilePermissions();
        Date date = new Date();
        date.setTime(entry.getEntryProperties().getLastModified());
        this.modificationDate = entry instanceof ParentDirectoryEntry
                ? new Date(Long.MIN_VALUE) : new Date(entry.getEntryProperties().getLastModified());
    }



}

