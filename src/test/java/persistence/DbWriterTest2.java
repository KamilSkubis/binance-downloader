package persistence;

import model.BinanceData;
import model.Symbol;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DbWriterTest2 {

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

        if(tables.contains("binance_data_seq")) {
            session.createSQLQuery("truncate binance_data_seq").executeUpdate();
            session.createSQLQuery("insert into test.binance_data_seq(next_val) values ( 0 )").executeUpdate();
        }
        session.getTransaction().commit();
        session.close();
    }

    @Before
    public void setUp() {
        dropTables();
    }

    @Test
    public void canAdd_oneBinanceData() {
        var symbol = new Symbol();
        symbol.setSymbolName("test");
        var binanceData = new BinanceData();
        binanceData.setSymbol(symbol);
        binanceData.setOpenTime(LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20));
        binanceData.setVolume(230.2);
        binanceData.setSymbol(symbol);
        binanceData.setOpen(323.41);
        binanceData.setHigh(23132.1);
        binanceData.setLow(231.3);
        binanceData.setClose(95.21);

        DbWriter dbWriter = new DbWriter(MySQLUtilTesting.getSessionFactory());
        dbWriter.write(binanceData);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> result = session.createQuery("From BinanceData").getResultList();
        assertTrue(result.get(0).getSymbol().equals(symbol));
        dropTables();
    }

    @Test
    public void canAdd_Symbol() {
        var symbol = new Symbol();
        symbol.setSymbolName("name");

        DbWriter dbWriter = new DbWriter(MySQLUtilTesting.getSessionFactory());
        dbWriter.write(symbol);

        var session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Symbol> result = session.createQuery("from Symbol").getResultList();
        assertEquals(result.get(0), symbol);
        dropTables();

    }


}
