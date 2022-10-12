package config;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;

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
        when(configLocation.getConfigInputStream()).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            reader.read(configLocation);
        });
    }


    @Test
    public void shouldReturn_ValidConfig(){
        ConfigLocation configLocation = mock(ConfigLocation.class);
        String s = "timeframe=1d\\n"
                + "kline_limit=1000\\n"
                + "startDateTime=2010-01-01:00:00:00Z\\n"
                +"url=jdbc:mysql://localhost:3306/test\\n"
                +"login=root\\n"
                +"password=password";

        when(configLocation.getConfigInputStream()).thenReturn(new ByteArrayInputStream(s.getBytes()));

        ConfigReader configReader = new ConfigReader();
        assertEquals("1d", configReader.read(configLocation).getTimeFrame());

    }

}
