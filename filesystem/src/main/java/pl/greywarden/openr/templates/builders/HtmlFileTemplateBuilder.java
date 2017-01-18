package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log4j
public class HtmlFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public HtmlFileTemplateBuilder(String path) {
        super(path);
    }

    @Override
    public void build() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<title></title>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\n" +
                "\t</body>\n" +
                "</html>";
        try {
            File target = new File(targetPath + ".html");
            FileUtils.touch(target);
            FileUtils.writeStringToFile(target, html, "UTF-8");
        } catch (IOException exception) {
            log.error("Could not create file with name " + targetPath + ".html", exception);
        }
    }

}
