package pl.greywarden.openr.filesystem;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class FileEntry extends AbstractEntry implements EntryOperations {

    public FileEntry(String path) {
        super(path);
    }

    @Override
    public void rename(String newName) {
        File targetFile = new File(getFilesystemEntry().getParentFile(), newName);
        try {
            FileUtils.moveFile(getFilesystemEntry(), targetFile);
            setFilesystemEntry(targetFile);
            setEntryProperties(new EntryProperties(targetFile));
        } catch (IOException exception) {
            log.error("Renaming file exception", exception);
        }
    }

    @Override
    public void copy() {
        clipboard = this;
        super.cut = false;
    }

    @Override
    public void cut() {
        clipboard = this;
        super.cut = true;
    }

    @Override
    public void paste(AbstractEntry target) {
        File targetFile = target.getFilesystemEntry();
        try {
            FileUtils.deleteQuietly(targetFile);
            FileUtils.copyFile(getFilesystemEntry(), targetFile);
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
            if (target.getEntryProperties().isDirectory()) {
                FileUtils.moveFileToDirectory(getFilesystemEntry(), target.getFilesystemEntry(), false);
                File result = new File(target.getFilesystemEntry(), super.getEntryProperties().getName());
                super.setEntryProperties(new EntryProperties(result));
                super.setFilesystemEntry(result);
            } else {
                FileUtils.moveFile(getFilesystemEntry(), target.getFilesystemEntry());
                super.setFilesystemEntry(target.getFilesystemEntry());
                super.setEntryProperties(new EntryProperties(target.getFilesystemEntry()));
            }
        } catch (IOException exception) {
            log.error("Move file exception", exception);
        }
    }

    @Override
    public void delete() {
        FileUtils.deleteQuietly(getFilesystemEntry());
    }

}
