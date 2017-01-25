package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Log4j
public class DocxFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public DocxFileTemplateBuilder(String targetPath) {
        super(targetPath);
    }

    @Override
    public void build() {
        XWPFDocument document = new XWPFDocument();
        try {
            File target = new File(targetPath + ".docx");
            FileOutputStream out = new FileOutputStream(target);
            document.write(out);
        } catch (IOException exception) {
            log.error("Could not create file with name " + targetPath + ".docx", exception);
        }
    }
}
