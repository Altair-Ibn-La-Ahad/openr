package pl.greywarden.openr.filesystem;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class DirectoryEntry extends AbstractEntry {

    public DirectoryEntry(String path) {
        super(path);
    }

    @Override
    public void move(AbstractEntry target) {
        try {
            FileUtils.deleteQuietly(target.getFilesystemEntry());
            FileUtils.moveDirectory(getFilesystemEntry(), target.getFilesystemEntry());
        } catch (IOException exception) {
            log.error("Move directory exception", exception);
        }
    }

}
