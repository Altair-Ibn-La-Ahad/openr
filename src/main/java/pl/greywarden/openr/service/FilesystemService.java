package pl.greywarden.openr.service;

import org.springframework.stereotype.Service;
import pl.greywarden.openr.domain.FilesystemEntry;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilesystemService {
    public List<FilesystemEntry> getContentOfDirectory(File directory) {
        return Stream.ofNullable(directory.listFiles())
                .flatMap(Stream::of)
                .map(this::toFilesystemEntry)
                .collect(Collectors.toList());
    }

    private FilesystemEntry toFilesystemEntry(File file) {
        var uri = file.toURI();
        var type = file.isFile() ? FilesystemEntry.EntryType.FILE : FilesystemEntry.EntryType.DIRECTORY;
        return new FilesystemEntry(uri, type, file.isHidden());
    }
}
