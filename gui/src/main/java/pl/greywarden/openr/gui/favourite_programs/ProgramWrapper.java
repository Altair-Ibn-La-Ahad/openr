package pl.greywarden.openr.gui.favourite_programs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProgramWrapper {

    private final String name;
    private final String path;
    private String icon;

    public ProgramWrapper(String name, String path, String icon) {
        this(name, path);
        this.icon = icon;
    }
}
