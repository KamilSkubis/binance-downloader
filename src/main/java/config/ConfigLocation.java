package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class ConfigLocation {

    private final File file;
    private final Logger logger;

    public ConfigLocation() {

        logger = LoggerFactory.getLogger(ConfigLocation.class);

        try {
            File DEFAULT_BASE = new File(FileConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            this.file = new File(DEFAULT_BASE, "settings.properties");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        logger.info("Using config file in path:" + file.getPath());
    }

    public ConfigLocation(File file) {
        logger = LoggerFactory.getLogger(ConfigLocation.class);

        if (!file.exists()) {
            throw (new RuntimeException(file + " doesn't exist"));
        }
        this.file = file;
        logger.info("Using config file in path:" + file.getPath());
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

