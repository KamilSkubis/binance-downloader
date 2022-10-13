package config;

import org.slf4j.Logger;

import java.time.Instant;
import java.util.Properties;

public final class Config {
    static Logger logger;
    private final String password;
    private final String timeframe;
    private final int klineLimit;
    private final Instant startDateTime;
    private final String url;
    private final String login;
    Properties properties;


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

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getTimeFrame() {
        return timeframe;
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

        @Override
        public String toString() {
            return "ConfigBuilder{" + "login='" + login + '\'' + ", url='" + url + '\'' + ", password=[***] " + '\'' + ", timeframe='" + timeframe + '\'' + ", klineLimit=" + klineLimit + ", startDateTime=" + startDateTime + '}';
        }

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

            if (this.klineLimit == null) {
                this.klineLimit = 500;
            }

            if (this.startDateTime == null) {
                startDateTime = Instant.parse("2010-01-01T00:00:00Z");
            }

            logger.info("Created new config");
            logger.info(this.toString());

            return new Config(this);
        }

    }

}