package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    Properties properties;

    public ConfigReader(){
        properties = new Properties();
        try {
            properties.load(new FileInputStream("settings.properties"));
        } catch (IOException e) {
            properties = null;
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
}