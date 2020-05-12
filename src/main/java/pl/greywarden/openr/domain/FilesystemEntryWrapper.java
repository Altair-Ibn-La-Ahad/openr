package pl.greywarden.openr.domain;

import lombok.Data;

@Data
public class FilesystemEntryWrapper {
    private final String name;
    private final String extension;
    private final String type;
    private final String size;
    private final String modified;
    private final String privileges;
}
