package config;

import java.io.IOException;
import java.util.Properties;

public class ConfigReader {


    public FileConfig read(ConfigLocation configLocation) {
        Properties properties = new Properties();
        try {
            properties.load(configLocation.getConfigInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FileConfig.ConfigBuilder()
                .setUrl(properties.getProperty("url"))
                .setLogin(properties.getProperty("login"))
                .setPassword(properties.getProperty("password"))
                .setTimeFrame(properties.getProperty("timeframe"))
                .setKlineLimit(properties.getProperty("kline_limit"))
                .setStartDateTime(properties.getProperty("startDateTime"))
                .build();
    }
}
