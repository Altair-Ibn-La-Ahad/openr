package pl.greywarden.openr.gui.menu.favourite_programs;

import javafx.scene.control.MenuItem;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import pl.greywarden.openr.commons.IconManager;
import pl.greywarden.openr.gui.favourite_programs.ProgramWrapper;

import java.io.IOException;

@Log4j
public class ProgramMenuItem extends MenuItem {

    @Getter
    private final ProgramWrapper program;

    public ProgramMenuItem(ProgramWrapper program) {
        super.setText(program.getName());
        this.program = program;
        super.setGraphic(IconManager.getSmallIconFromPath(program.getIcon()));
        super.setOnAction(event -> startProgram());
    }

    private void startProgram() {
        try {
            new ProcessBuilder(program.getPath()).start();
        } catch (IOException e) {
            log.error("Exception during starting program", e);
        }
    }
}
