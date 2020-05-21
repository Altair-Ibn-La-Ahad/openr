package pl.greywarden.openr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.kordamp.ikonli.feather.Feather;

import java.math.BigInteger;

@With
@Data
@AllArgsConstructor
public class FilesystemEntryWrapper {
    private String name;
    private String extension;
    private BigInteger size;
    private String modified;
    private String privileges;
    private String path;

    private FilesystemEntry.EntryType type;
    private String icon;

    public FilesystemEntryWrapper() {
        this.name = "..";
        this.extension = "..";
        this.size = BigInteger.valueOf(Long.MIN_VALUE);
        this.type = FilesystemEntry.EntryType.DIRECTORY;
        this.icon = Feather.FTH_FOLDER.getDescription();
    }

    public boolean isParentDirectoryEntry() {
        return "..".equals(name);
    }
}
