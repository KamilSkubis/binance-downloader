import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import persistence.MySQLUtilTesting;
import persistence.SchemaInitializer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SchemaInitializerTest {

    @Before
    public void before() {
        UtilForTesting.dropTables();
    }

    @Test
    public void initTables_whenNoTables_IntegrationTest() {
        SchemaInitializer schemaInitializer = new SchemaInitializer(MySQLUtilTesting.getSessionFactory());
        schemaInitializer.initializeSchemasOrDoNothing();

        var s = MySQLUtilTesting.getSessionFactory().openSession();
        var list = s.createSQLQuery("show tables").getResultList();
        System.out.println("Result:" + list.toString());

        assertEquals(3, list.size());
        assertTrue(list.contains("binance_data"));
        assertTrue(list.contains("binance_data_seq"));
        assertTrue(list.contains("symbols"));
    }


    @After
    public void after() {
        UtilForTesting.dropTables();
    }

}
