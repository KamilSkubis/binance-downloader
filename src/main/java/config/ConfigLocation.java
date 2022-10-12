package config;

import java.io.File;
import java.net.URISyntaxException;

class ConfigLocation {

    private final File file;

    public ConfigLocation() {
        try {
            File DEFAULT_BASE = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            this.file = new File(DEFAULT_BASE, "settings.properties");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public ConfigLocation(File file) {
        if(!file.exists()){
            throw(new RuntimeException(file + " doesn't exist"));
        }
        this.file = file;
    }

    public File getConfig() {
        return file;
    }
}

