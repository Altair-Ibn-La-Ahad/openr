package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class EmptyFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public EmptyFileTemplateBuilder(String path) {
        super(path);
    }

    @Override
    public void build() {
        try {
            File target = new File(targetPath);
            FileUtils.touch(target);
        } catch (IOException exception) {
            log.error("Could not create file with name " + targetPath, exception);
        }
    }
}
