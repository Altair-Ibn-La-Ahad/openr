package pl.greywarden.openr.domain;

import java.util.ArrayList;
import java.util.Collection;

public class DirectoryContent extends ArrayList<FilesystemEntryWrapper> {
    private boolean hasParent;

    public DirectoryContent(Collection<? extends FilesystemEntryWrapper> entries) {
        super(entries);
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = true;
    }

    public boolean hasParent() {
        return this.hasParent;
    }
}
