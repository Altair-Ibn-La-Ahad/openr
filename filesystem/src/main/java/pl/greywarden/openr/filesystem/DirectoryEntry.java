package pl.greywarden.openr.filesystem;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class DirectoryEntry extends AbstractEntry implements EntryOperations {

    public DirectoryEntry(String path) {
        super(path);
    }

    @Override
    public void copy() {
        clipboard = this;
    }

    @Override
    public void cut() {
        copy();
        cut = true;
    }

    @Override
    public void paste(AbstractEntry target) {
        File targetDirectory = target.getFilesystemEntry();
        try {
            FileUtils.deleteQuietly(targetDirectory);
            FileUtils.copyFile(getFilesystemEntry(), targetDirectory);
            if (cut) {
                FileUtils.deleteQuietly(getFilesystemEntry());
                clipboard = null;
            }
        } catch (IOException exception) {
            log.error("Paste file exception", exception);
        }
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

    @Override
    public void delete() {
        FileUtils.deleteQuietly(getFilesystemEntry());
    }
}
