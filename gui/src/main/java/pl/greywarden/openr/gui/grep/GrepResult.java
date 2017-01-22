package pl.greywarden.openr.gui.grep;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class GrepResult {

    private final File file;
    private final String text;

}
