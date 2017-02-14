package pl.greywarden.openr.templates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.greywarden.openr.templates.builders.AbstractTemplateFileBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
@AllArgsConstructor
public class Template {

    @Getter
    private final String name;
    @Getter
    private final String type;
    private final String templateBuilderClassName;

    @SuppressWarnings("unchecked")
    public void build(String target) {
        try {
            Class c = Class.forName(templateBuilderClassName);
            AbstractTemplateFileBuilder templateFileBuilder =
                    (AbstractTemplateFileBuilder) c.getDeclaredConstructor(String.class)
                            .newInstance(target);
            templateFileBuilder.build();
        } catch (Exception exception) {
            log.error("Template builder implementation not found", exception);
        }
    }

    public static List<Template> getAvailableTemplatesList() {
        try {
            List <Template> result = new ArrayList<>();
            InputStream inputStream = Template.class.getClassLoader()
                    .getResourceAsStream("templates/file-templates.json");
            String jsonString = IOUtils.toString(inputStream, "UTF-8");
            JSONArray templates = new JSONObject(jsonString).getJSONArray("templates");
            templates.forEach(o -> {
                JSONObject template = (JSONObject) o;
                result.add(new Template(template.getString("name"),
                                        template.getString("type"),
                                        template.getString("builder")));
            });
            return result;
        } catch (IOException exception) {
            log.error("Get available templates exception", exception);
            throw new RuntimeException(exception);
        }
    }

    private static Map<String, List<Template>> getAvailableTemplates() {
        return getAvailableTemplatesList().stream().collect(Collectors.groupingBy(Template::getType));
    }

    public static List<Template> getTemplatesByType(String type) {
        return getAvailableTemplates().get(type);
    }

}
