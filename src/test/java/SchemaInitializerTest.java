import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import persistence.MySQLUtilTesting;
import persistence.SchemaInitializer;

import java.util.Arrays;

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

        Assertions.assertEquals(2, list.size());
    }


    @After
    public void after() {
        UtilForTesting.dropTables();
    }

}
