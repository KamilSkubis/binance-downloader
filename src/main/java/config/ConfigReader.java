package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class ConfigReader {
    Properties properties;
    Logger logger;

    public ConfigReader(){
        logger = LoggerFactory.getLogger(ConfigReader.class);
        properties = new Properties();
        try {
            File base = new File(ConfigReader.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File configFile = new File(base, "settings.properties");
            properties.load(new FileInputStream(configFile));

            System.out.println(properties.isEmpty());
            System.out.println("size properties: " + properties.size());

            logger.info("loaded new settings" + properties.toString());
            logger.info("url  " + properties.get("url"));
            logger.info("password " + properties.get("password"));
            logger.info("login: " + properties.get("login"));
        } catch (IOException e) {
            properties = null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public boolean userSettingsExists() {
        if(properties != null){
            return true;
        }else{
            return false;
        }
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public String getLogin() {
        return properties.getProperty("login");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public String getTimeFrame(){
        return properties.getProperty("timeframe");
    }

    public String getKlineLimit(){
        return properties.getProperty("kline_limit");
    }

}