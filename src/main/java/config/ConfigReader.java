package config;

public class ConfigReader {

    public Config read(ConfigLocation configLocation) {



        return new Config.ConfigBuilder().build();
    }
}
