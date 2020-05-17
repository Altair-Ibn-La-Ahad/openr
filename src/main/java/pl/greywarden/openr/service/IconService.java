package pl.greywarden.openr.service;

import lombok.SneakyThrows;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.material.Material;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class IconService {
    @SneakyThrows
    public String getIcon(File file) {
        if (file.isDirectory()) {
            return Material.FOLDER.getDescription();
        } else {
            return Feather.FTH_FILE.getDescription();
        }
    }
}
