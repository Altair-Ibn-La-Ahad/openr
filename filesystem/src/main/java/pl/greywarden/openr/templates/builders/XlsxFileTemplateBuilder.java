package pl.greywarden.openr.templates.builders;

import lombok.extern.log4j.Log4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Log4j
public class XlsxFileTemplateBuilder extends AbstractTemplateFileBuilder {

    public XlsxFileTemplateBuilder(String path) {
        super(path);
    }

    @Override
    public void build() {
        XSSFWorkbook document = new XSSFWorkbook();
        try {
            File target = new File(targetPath + ".xlsx");
            FileOutputStream out = new FileOutputStream(target);
            document.write(out);
        } catch (IOException exception) {
            log.error("Could not create file with name " + targetPath + ".xslx", exception);
        }
    }
}
