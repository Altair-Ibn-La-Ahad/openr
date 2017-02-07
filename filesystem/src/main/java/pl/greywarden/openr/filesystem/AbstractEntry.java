package pl.greywarden.openr.filesystem;

import com.sun.jna.platform.win32.W32FileUtils;
import javafx.beans.binding.BooleanBinding;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Log4j
public abstract class AbstractEntry implements EntryOperations {

    protected final File filesystemEntry;
    private final EntryProperties entryProperties;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected static AbstractEntry clipboard;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected static boolean cut;

    protected AbstractEntry(String path) {
        filesystemEntry = new File(path);
        entryProperties = new EntryProperties(filesystemEntry);
    }

    public void paste(AbstractEntry target) {
        try {
            if (clipboard.getEntryProperties().isDirectory()) {
                FileUtils.copyDirectoryToDirectory(clipboard.filesystemEntry, target.filesystemEntry);
            } else {
                FileUtils.copyFileToDirectory(clipboard.filesystemEntry, target.filesystemEntry);
            }
            if (cut) {
                FileUtils.deleteQuietly(clipboard.filesystemEntry);
            }
        } catch (IOException exception) {
            log.error("Exception during paste", exception);
        }
    }

    public void copy() {
        clipboard = this;
        cut = false;
    }

    public void cut() {
        clipboard = this;
        cut = true;
    }

    public static BooleanBinding clipboardEmptyBinding() {
        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return clipboard == null;
            }
        };
    }

    public void delete() {
        FileUtils.deleteQuietly(getFilesystemEntry());
    }

    public void moveToTrash() {
        if (SystemUtils.IS_OS_LINUX) {
            moveToTrashLinux();
        } else if (SystemUtils.IS_OS_WINDOWS) {
            moveToTrashWindows();
        }
    }

    private void moveToTrashLinux() {
        String env = System.getenv("XDG_DATA_HOME");
        if (env == null) {
            env = System.getenv("HOME") + "/.local/share";
        }
        String pathToTrash = env + "/Trash";
        StringBuilder trashInfo = new StringBuilder();
        trashInfo.append("[Trash Info]").append(System.lineSeparator());
        trashInfo.append("Path=").append(entryProperties.getAbsolutePath()).append(System.lineSeparator());
        trashInfo.append("DeletionDate=").append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
        try {
            File trashInfoFile = new File(pathToTrash, "/info/" + entryProperties.getName() + ".trashinfo");
            FileUtils.touch(trashInfoFile);
            Files.write(trashInfoFile.toPath(), trashInfo.toString().getBytes());
            FileUtils.moveToDirectory(filesystemEntry, new File(pathToTrash, "files"), false);
        } catch (IOException e) {
            log.error("Exception during moving to trash", e);
        }
    }

    private void moveToTrashWindows() {
        try {
            new W32FileUtils().moveToTrash(new File[]{filesystemEntry});
        } catch (IOException e) {
            log.error("Exception during moving to trash", e);
        }
    }

    public boolean existsInTargetDirectory(DirectoryEntry target) {
        String filename = this.getFilesystemEntry().getName();
        String root = target.getEntryProperties().getAbsolutePath();
        return new File(root, filename).exists();
    }

    public static void clearClipboard() {
        clipboard = null;
    }
}
