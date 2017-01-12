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
    public void copy() {
        clipboard = this;
        super.cut = false;
    }

    @Override
    public void cut() {
        copy();
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
        try{
            FileUtils.moveFile(getFilesystemEntry(), target.getFilesystemEntry());
        } catch (IOException exception) {
            log.error("Rename file exception", exception);
        }
    }

    @Override
    public void delete() {
        FileUtils.deleteQuietly(getFilesystemEntry());
    }

}
