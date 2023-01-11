package config;

import java.time.Instant;

public interface Config {
    boolean userSettingsExists();

    String getUrl();

    String getLogin();

    String getPassword();

    String getTimeFrame();

    Integer getKlineLimit();

    Instant getStartDateTime();
}
