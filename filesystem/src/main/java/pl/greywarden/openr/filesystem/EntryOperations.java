package pl.greywarden.openr.filesystem;

public interface EntryOperations {

    void rename(String newName);

    void copy();
    void cut();
    void paste(AbstractEntry target);
    void move(AbstractEntry target);
    void delete();

}
