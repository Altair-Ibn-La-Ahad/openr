package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Log4j
public class PptxFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public PptxFileTemplateBuilder(String targetPath) {
        super(targetPath);
    }

    @Override
    public void build() {
        XMLSlideShow slideShow = new XMLSlideShow();
        try {
            File targetFile = new File(targetPath + ".pptx");
            FileOutputStream out = new FileOutputStream(targetFile);
            slideShow.write(out);
        } catch (IOException e) {
            log.error("Could not create file with name " + targetPath + ".pptx", e);
        }
    }
}
