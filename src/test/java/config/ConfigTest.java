package config;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigTest {

    @TempDir
    File tempDir;
    File file;

    @Before
    public void before() {
        file = new File(tempDir, "settings.properties");
    }

    @Test
    public void shouldThrowErrorWhen_IssueWithFile() {
        ConfigReader reader = new ConfigReader();

        ConfigLocation configLocation = mock(ConfigLocation.class);
        when(configLocation.getConfig()).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            reader.read(configLocation);
        });
    }

}
