package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class TextFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public TextFileTemplateBuilder(String path) {
        super(path);
    }

    @Override
    public void build() {
        String extension = ".txt";
        try {
            FileUtils.touch(new File(targetPath + extension));
        } catch (IOException exception) {
            log.error("Could not create file with name " + targetPath + extension, exception);
        }
    }
}
