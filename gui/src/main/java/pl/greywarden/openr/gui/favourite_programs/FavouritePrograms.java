package pl.greywarden.openr.gui.favourite_programs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavouritePrograms {

    private static final Collection<ProgramWrapper> programs = new LinkedList<>();

    static {
        loadFromFile();
    }

    private static void loadFromFile() {
        File sourceXml = getProgramsXml();
        SAXBuilder builder = new SAXBuilder();

        try {
            Document xml = builder.build(sourceXml);
            Element root = xml.getRootElement();
            root.getChildren().forEach(element -> programs.add(new ProgramWrapper(
                    element.getAttributeValue("name"),
                    element.getAttributeValue("path"),
                    element.getAttributeValue("icon"))));
        } catch (IOException | JDOMException exception) {
            log.error("Exception during loading favourite programs", exception);
        }
    }


    private static File getProgramsXml() {
        URL configFileUrl = FavouritePrograms.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            File parentDir = new File(configFileUrl.toURI()).getParentFile();
            return new File(parentDir, "programs.xml");
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void storeProgramsToFile() {
        File targetXml = getProgramsXml();

        Document xml = new Document(new Element("programs"));
        programs.forEach(program -> {
            Element element = new Element("program");
            element.setAttribute("name", program.getName());
            element.setAttribute("path", program.getPath());
            element.setAttribute("icon", program.getIcon());
            xml.getRootElement().addContent(element);
        });

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());

        try {
            OutputStream out = new FileOutputStream(targetXml);
            outputter.output(xml, out);
        } catch (IOException exception) {
            log.error("Unable to store programs to xml", exception);
        }
    }

    public static void add(ProgramWrapper program) {
        programs.add(program);
    }

    public static void remove(ProgramWrapper program) {
        programs.remove(program);
    }

    public static Collection<ProgramWrapper> getPrograms() {
        return programs.stream().sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .collect(Collectors.toList());
    }
}
