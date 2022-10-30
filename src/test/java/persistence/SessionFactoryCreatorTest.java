package persistence;

import config.Config;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SessionFactoryCreatorTest {


    @Test
    public void shouldReturnSessionFactoryObj() {
        Config config = new Config.ConfigBuilder()
                .setUrl("jdbc:mysql://localhost:3306/test")
                .setLogin("root")
                .setPassword("password")
                .setTimeFrame("4h")
                .build();

        SessionFactoryCreator sfc = new SessionFactoryCreator(config);
        assertNotNull(sfc.getSessionFactory());
    }

}
