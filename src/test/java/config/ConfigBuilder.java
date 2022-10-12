package config;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigBuilder {

    @Test
    public void shouldThrowException_whenNullUrl() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> {
            new Config.ConfigBuilder()
                    .setLogin("login")
                    .setPassword("pass")
                    .setKlineLimit("321")
                    .setStartDateTime("2000")
                    .setTimeFrame("4h")
                    .build();
        });
    }

    @Test
    public void shouldThrowException_whenNullLogin() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> {
            new Config.ConfigBuilder()
                    .setUrl("test")
                    .setPassword("pass")
                    .setKlineLimit("321")
                    .setStartDateTime("2000")
                    .setTimeFrame("4h")
                    .build();
        });
    }

    @Test
    public void shouldThrowException_whenNullPassword() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> {
            new Config.ConfigBuilder()
                    .setUrl("test")
                    .setLogin("login")
                    .setKlineLimit("321")
                    .setStartDateTime("2000")
                    .setTimeFrame("4h")
                    .build();
        });
    }

    @Test
    public void shouldThrowException_whenNullTimeFrame() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> {
            new Config.ConfigBuilder()
                    .setUrl("test")
                    .setLogin("login")
                    .setPassword("pass")
                    .setKlineLimit("321")
                    .setStartDateTime("2000")
                    .build();
        });
    }

    @Test
    public void shouldReturn500_whenNullKline_limit() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setStartDateTime("2000")
                .build();
        assertEquals("500",config.getKlineLimit());
    }

    @Test
    public void canReadProperKlineLimit() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setStartDateTime("2000")
                .setKlineLimit("234")
                .build();
        assertEquals(Optional.of(234),config.getKlineLimit());
    }

    @Test
    public void shouldReturnSomeDate_whenNullStartDate() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .build();
        assertEquals("2010-01-01",config.getStartDateTime());
    }

    @Test
    public void shouldReadStartDate() {
        Config config = new Config.ConfigBuilder()
                .setUrl("test")
                .setLogin("login")
                .setPassword("pass")
                .setTimeFrame("time")
                .setStartDateTime("2000")
                .setKlineLimit("234")
                .setStartDateTime("2020-01-01")
                .build();
        assertEquals("2020-01-01",config.getKlineLimit());
    }

    @Test
    public void dateIsValid(){

    }



}