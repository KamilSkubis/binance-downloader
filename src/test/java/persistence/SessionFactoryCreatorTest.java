package persistence;

import config.FileConfig;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SessionFactoryCreatorTest {


    @Test
    public void shouldReturnSessionFactoryObj() {
        FileConfig fileConfig = new FileConfig.ConfigBuilder()
                .setUrl("jdbc:mysql://localhost:3306/test")
                .setLogin("root")
                .setPassword("password")
                .setTimeFrame("4h")
                .build();

        SessionFactoryCreator sfc = new SessionFactoryCreator(fileConfig);
        assertNotNull(sfc.getSessionFactory());
    }

}
