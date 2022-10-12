package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    public InputStream getConfigInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    return inputStream;
    }
}

