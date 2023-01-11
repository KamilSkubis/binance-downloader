package integration;

import config.Config;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import persistence.MySQLUtilTesting;

import java.time.Instant;

public class IntegrationTest {

    public static void dropTables() {
        Session session = MySQLUtilTesting.getSessionFactory().openSession();

        final var tables = session.createSQLQuery("Show tables")
                .getResultList();

        session.beginTransaction();
        if (tables.contains("binance_data")) {
            session.createSQLQuery("truncate TABLE binance_data").executeUpdate();
        }
        if (tables.contains("symbols")) {
            session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
            session.createSQLQuery("truncate TABLE symbols").executeUpdate();
            session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        }

        session.getTransaction().commit();
        session.close();
    }

    @Before
    public void setUp() {
        dropTables();
    }


    @Test
    public void add2rows_oneSymbol_inTwoSessions_should_OpenTime_same_delta() {
        Config config = new ConfigStub();


    }

}

class ConfigStub implements Config {

    @Override
    public boolean userSettingsExists() {
        return false;
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://localhost:3306/test";
    }

    @Override
    public String getLogin() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getTimeFrame() {
        return "1d";
    }

    @Override
    public Integer getKlineLimit() {
        return 1000;
    }

    @Override
    public Instant getStartDateTime() {
        return Instant.now();
    }
}