import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import persistence.MySQLUtilTesting;
import persistence.SchemaInitializer;

public class SchemaInitializerTest {

    @Before
    public void before() {
        UtilForTesting.dropTables();
    }

    @Test
    public void initTables_whenNoTables_IntegrationTest() {
        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        SchemaInitializer schemaInitializer = new SchemaInitializer(session);
        schemaInitializer.initializeSchemasOrDoNothing();

        var s = MySQLUtilTesting.getSessionFactory().openSession();

        int size = s.createSQLQuery("show tables").getResultList().size();
        Assertions.assertEquals(2, size);
    }


    @After
    public void after() {
        UtilForTesting.dropTables();
    }

}
