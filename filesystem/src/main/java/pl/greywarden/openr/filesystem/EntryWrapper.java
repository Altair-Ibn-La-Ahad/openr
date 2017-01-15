package pl.greywarden.openr.filesystem;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

@Getter
public class EntryWrapper {

    private final String name;
    private final String extension;
    private final String size;
    private final String modificationDate;
    private final String privileges;
    private final AbstractEntry entry;

    public EntryWrapper(AbstractEntry entry) {
        this.entry = entry;
        this.name = entry.getEntryProperties().isDirectory()
                ? entry.getEntryProperties().getName()
                : FilenameUtils.getBaseName(entry.getEntryProperties().getName());
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

