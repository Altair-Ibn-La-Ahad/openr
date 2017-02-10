package pl.greywarden.openr.gui.favourite_programs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Program {

    private final String name;
    private final String path;
    private String icon;

    public Program(String name, String path, String icon) {
        this(name, path);
        this.icon = icon;
    }
}
