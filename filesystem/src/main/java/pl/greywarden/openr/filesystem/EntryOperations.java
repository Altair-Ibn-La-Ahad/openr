package pl.greywarden.openr.filesystem;

public interface EntryOperations {

    void paste(AbstractEntry target);
    void move(AbstractEntry target);
    void delete();
    void moveToTrash();

}
