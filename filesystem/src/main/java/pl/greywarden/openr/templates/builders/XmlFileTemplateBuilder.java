package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class XmlFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public XmlFileTemplateBuilder(String path) {
        super(path);
    }

    @Override
    public void build() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        try {
            File target = new File(targetPath + ".xml");
            FileUtils.touch(target);
            FileUtils.writeStringToFile(target, xml, "UTF-8");
        } catch (IOException exception) {
            log.error("Could not create file with name " + targetPath + ".xml", exception);
        }
    }

}
