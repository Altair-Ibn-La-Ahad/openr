package pl.greywarden.openr.filesystem;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;

@Log4j
@AllArgsConstructor
public class EntryProperties {

    private final File filesystemEntry;

    public boolean isReadable() {
        return filesystemEntry.canRead();
    }

    public boolean isWritable() {
        return filesystemEntry.canWrite();
    }

    public boolean isExecutable() {
        return filesystemEntry.canExecute();
    }

    public String getName() {
        return filesystemEntry.getName();
    }

    public String getAbsolutePath() {
        return filesystemEntry.getAbsolutePath();
    }

    public String getOwner() {
        try {
            return Files.getOwner(filesystemEntry.toPath()).getName();
        } catch (IOException exception) {
            log.error("Get owner exception", exception);
            return null;
        }
    }

    public long getSizeInBytes() {
        return FileUtils.sizeOf(filesystemEntry);
    }

    public String getPosixFilePermissions() {
        try {
            PosixFileAttributes attributes = Files.readAttributes(filesystemEntry.toPath(), PosixFileAttributes.class);
            String result = PosixFilePermissions.toString(attributes.permissions());
            if (filesystemEntry.isDirectory()) {
                result = "d" + result;
            } else {
                if (FileUtils.isSymlink(filesystemEntry)) {
                    result = "l" + result;
                } else {
                    result = "-" + result;
                }
            }
            return result;
        } catch (IOException exception) {
            log.error("Get file permissions exception", exception);
            return null;
        }
    }

    public String getDosFilePermissions() {
        try {
            DosFileAttributes attributes = Files.readAttributes(filesystemEntry.toPath(), DosFileAttributes.class);
            return (attributes.isArchive() ? "A" : "-") +
                    (attributes.isHidden() ? "H" : "-") +
                    (attributes.isReadOnly() ? "R" : "-") +
                    (attributes.isSystem() ? "S" : "-");
        } catch (IOException exception) {
            log.error("Get file permissions exception", exception);
            return null;
        }
    }

    public long getLastModified() {
        return filesystemEntry.lastModified();
    }

    public boolean hasParent() {
        return filesystemEntry.isDirectory() && filesystemEntry.getParent() != null;
    }

    public boolean isDirectory() {
        return filesystemEntry.isDirectory();
    }

    public String getMimeType() {
        try{
            return Files.probeContentType(filesystemEntry.toPath());
        } catch (IOException exception) {
            log.error("Get MIME type exception", exception);
            return null;
        }
    }

    public String getExtension() {
        return filesystemEntry.isDirectory() ? "" : FilenameUtils.getExtension(filesystemEntry.getAbsolutePath());
    }

}
