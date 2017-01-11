package pl.greywarden.openr.filesystem;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileEntryTest {

    private static final String testDirectory = SystemUtils.USER_HOME + "/test-files/";

    private void cleanTestDirectory() {
        FileUtils.listFiles(
                new File(testDirectory), FileFileFilter.FILE, DirectoryFileFilter.DIRECTORY)
                .forEach(FileUtils::deleteQuietly);
    }

    private File createFile(String name) throws IOException {
        File result = new File(testDirectory + name);
        Assert.assertTrue("Failed to create file", result.createNewFile());
        return result;
    }

    @Before
    public void setUp() throws Exception {
        File testFilesDirectory = new File(testDirectory);
        Assert.assertTrue("Failed to create test directory", testFilesDirectory.mkdir());
        cleanTestDirectory();
    }

    @Test
    public void rename() throws Exception {
        String initialName = "rename";
        String resultName = "rename1";
        File file = createFile("rename");
        FileEntry fileEntry = new FileEntry(file.getAbsolutePath());

        fileEntry.rename(resultName);
        Assert.assertEquals("Renaming from initial to target failed",
                resultName, fileEntry.getEntryProperties().getName());

        fileEntry.rename(initialName);
        Assert.assertEquals("Reverting rename failed",
                initialName, fileEntry.getEntryProperties().getName());
    }

    @Test
    public void copy() throws Exception {
        File file = createFile("copy");
        FileEntry fileEntry = new FileEntry(file.getAbsolutePath());
        fileEntry.copy();
        Assert.assertFalse("Clipboard is empty", fileEntry.isClipboardEmpty());
    }

    @Test
    public void cut() throws Exception {
        File file = createFile("cut");
        FileEntry fileEntry = new FileEntry(file.getAbsolutePath());
        fileEntry.cut();
        Assert.assertTrue("Cut flag is not set", fileEntry.cut);
        Assert.assertFalse("Clipboard is empty", fileEntry.isClipboardEmpty());
    }

    @Test
    public void paste() throws Exception {
        FileEntry fileEntry = new FileEntry(createFile("paste").getAbsolutePath());
        FileEntry targetFile = new FileEntry(createFile("paste1").getAbsolutePath());

        fileEntry.copy();
        fileEntry.paste(targetFile);

        Assert.assertTrue("Copying file failed", targetFile.getFilesystemEntry().exists());
    }

    @Test
    public void move() throws Exception {
        File subdirectory = new File(testDirectory, "sub");
        Assert.assertTrue("Failed to create test subdirectory", subdirectory.mkdir());

        FileEntry fileEntry = new FileEntry(createFile("move").getAbsolutePath());
        FileEntry target = new FileEntry(subdirectory.getAbsolutePath());

        fileEntry.move(target);
        Assert.assertTrue("File doesn't exist", fileEntry.getFilesystemEntry().exists());
    }

    @Test
    public void delete() throws Exception {
        FileEntry fileEntry = new FileEntry(createFile("delete").getAbsolutePath());
        fileEntry.delete();
        Assert.assertFalse("File still exists", fileEntry.getFilesystemEntry().exists());
    }

    @After
    public void tearDown() throws Exception {
        cleanTestDirectory();
        FileUtils.deleteDirectory(new File(testDirectory));
    }

}