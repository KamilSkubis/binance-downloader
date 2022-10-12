package config;

import org.slf4j.Logger;

import java.time.Instant;
import java.util.Properties;

public final class Config {
    private final String password;
    private final String timeframe;
    private final int klineLimit;
    private final Instant startDateTime;
    private final String url;
    private final String login;
    Properties properties;
    Logger logger;


    private Config(ConfigBuilder builder) {
        this.url = builder.url;
        this.login = builder.login;
        this.password = builder.password;
        this.timeframe = builder.timeframe;
        this.klineLimit = builder.klineLimit;
        this.startDateTime = builder.startDateTime;
    }

    public boolean userSettingsExists() {
        return properties != null;
    }


//    public Config(){
//        logger = LoggerFactory.getLogger(Config.class);
//        properties = new Properties();
//        try {
//            File base = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
//            File configFile = new File(base, "settings.properties");
//            properties.load(new FileInputStream(configFile));
//
//
//            logger.info("loaded new settings" + properties.toString());
//            logger.info("url  " + properties.get("url"));
//            logger.info("password: ***********");
//            logger.info("login: *********** ");
//        } catch (IOException e) {
//            properties = null;
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public String getTimeFrame() {
        return properties.getProperty("timeframe");
    }

    public Integer getKlineLimit() {
        return klineLimit;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public static class ConfigBuilder {

        private String login;
        private String url;
        private String password;
        private String timeframe;
        private Integer klineLimit;
        private Instant startDateTime;

        public ConfigBuilder setLogin(String login) {
            this.login = login;
            return this;
        }

        public ConfigBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public ConfigBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public ConfigBuilder setTimeFrame(String timeFrame) {
            this.timeframe = timeFrame;
            return this;
        }

        public ConfigBuilder setKlineLimit(String klineLimit) {
            this.klineLimit = Integer.valueOf(klineLimit);
            return this;
        }

        public ConfigBuilder setStartDateTime(String startDateTime) {
            this.startDateTime = Instant.parse(startDateTime);
            return this;
        }

        public Config build() {
            //validate necessary fields
            if (url == null || login == null || password == null || timeframe == null) {
                throw new IllegalArgumentException("Check url, login, password,timeframe parameters in settings file");
            }
            if(klineLimit == null){
                klineLimit = 500;
            }

            if(startDateTime == null){
                startDateTime = Instant.parse("2010-01-01T00:00:00Z");
            }
            return new Config(this);
        }

    }

}