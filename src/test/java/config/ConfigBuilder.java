package config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigBuilder {

    @Test
    public void shouldThrowException_whenNullUrl() {
        assertThrows(IllegalArgumentException.class, () -> new Config.ConfigBuilder()
                .setLogin("login")
                .setPassword("pass")
                .setKlineLimit("321")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .setTimeFrame("4h")
                .build());
    }

    @Test
    public void shouldThrowException_whenNullLogin() {
        assertThrows(IllegalArgumentException.class, () -> new Config.ConfigBuilder()
                .setUrl("test")
                .setPassword("pass")
                .setKlineLimit("321")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .setTimeFrame("4h")
                .build());
    }

    @Test
    public void shouldThrowException_whenNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setKlineLimit("321")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .setTimeFrame("4h")
                .build());
    }

    @Test
    public void shouldThrowException_whenNullTimeFrame() {
        assertThrows(IllegalArgumentException.class, () -> new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setKlineLimit("321")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .build());
    }

    @Test
    public void shouldReturn500_whenNullKline_limit() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .build();
        Assertions.assertEquals(500, config.getKlineLimit());
    }

    @Test
    public void canReadProperKlineLimit() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .setKlineLimit("234")
                .build();
        Assertions.assertEquals(Optional.of(234).get(), config.getKlineLimit());
    }

    @Test
    public void shouldReturnSomeDate_whenNullStartDate() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .build();
        Assertions.assertEquals(Instant.parse("2010-01-01T00:00:00.00Z"), config.getStartDateTime());
    }

    @Test
    public void shouldReadStartDate() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setKlineLimit("234")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .build();
        Assertions.assertEquals(Instant.parse("2000-01-01T00:00:00.00Z"), config.getStartDateTime());
    }

    @Test
    public void dateIsValid() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setKlineLimit("234")
                .setStartDateTime("2000-01-01T00:00:00.00Z")
                .build();
        Assertions.assertEquals(Instant.parse("2000-01-01T00:00:00.00Z")
                ,config.getStartDateTime());
    }


}