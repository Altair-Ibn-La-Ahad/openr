package pl.greywarden.openr.gui.scenes.main_window.menu.favourite_programs;

import javafx.scene.control.MenuItem;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.favourite_programs.Program;

import java.io.IOException;

@Log4j
public class ProgramMenuItem extends MenuItem {

    @Getter
    private final Program program;

    public ProgramMenuItem(Program program) {
        super.setText(program.getName());
        this.program = program;
        super.setGraphic(IconManager.getIconFromPath(program.getIcon()));
        super.setOnAction(event -> {
            try {
                new ProcessBuilder(program.getPath()).start();
            } catch (IOException e) {
                log.error("Exception during starting program", e);
            }
        });
    }
}
