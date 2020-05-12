package pl.greywarden.openr.service;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import pl.greywarden.openr.domain.FilesystemEntry;
import pl.greywarden.openr.domain.FilesystemEntryWrapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FilesystemEntryWrapperFactory {
    public FilesystemEntryWrapper wrap(FilesystemEntry entry) {
        var uri = entry.getUri();
        var type = entry.getType().name();
        var file = new File(uri);

        var name = getName(file);
        var extension = getExtension(file);
        var size = getSize(file);
        var modified = getLastModified(file);
        var privileges = getPrivileges(file);

        return new FilesystemEntryWrapper(
                name,
                extension,
                type,
                size,
                modified,
                privileges
        );
    }

    @SneakyThrows
    private String getPrivileges(File file) {
        var attributes = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
        var result = PosixFilePermissions.toString(attributes.permissions());
        if (file.isDirectory()) {
            result = "d" + result;
        } else {
            if (FileUtils.isSymlink(file)) {
                result = "l" + result;
            } else {
                result = "-" + result;
            }
        }
        return result;
    }

    @SneakyThrows
    private String getLastModified(File file) {
        var lastModified = Files.getLastModifiedTime(file.toPath());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lastModified.toMillis()));
    }

    private String getSize(File file) {
        return FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file));
    }

    private String getExtension(File file) {
        return file.isDirectory() ? "" : FilenameUtils.getExtension(file.getName());
    }

    private String getName(File file) {
        var fileName = file.getName();
        var baseName = FilenameUtils.getBaseName(fileName);
        return baseName.isEmpty() ? fileName : baseName;
    }
}
