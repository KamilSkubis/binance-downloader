package persistence;

import model.BinanceData;
import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Test
    public void batchInsertTest() {
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

        var b2 = new BinanceData();
        b2.setSymbol(symbol);
        b2.setOpenTime(LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20));
        b2.setVolume(230.2);
        b2.setSymbol(symbol);
        b2.setOpen(323.41);
        b2.setHigh(23132.1);
        b2.setLow(231.3);
        b2.setClose(95.21);

        var b3 = new BinanceData();
        b3.setSymbol(symbol);
        b3.setOpenTime(LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20));
        b3.setVolume(230.2);
        b3.setSymbol(symbol);
        b3.setOpen(323.41);
        b3.setHigh(23132.1);
        b3.setLow(231.3);
        b3.setClose(95.21);

        var b4 = new BinanceData();
        b4.setSymbol(symbol);
        b4.setOpenTime(LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20));
        b4.setVolume(230.2);
        b4.setSymbol(symbol);
        b4.setOpen(323.41);
        b4.setHigh(23132.1);
        b4.setLow(231.3);
        b4.setClose(95.21);

        DbWriter dbWriter = new DbWriter(MySQLUtilTesting.getSessionFactory());
        List<Data> l = new ArrayList<>();
        l.add(binanceData);
        l.add(b2);
        l.add(b3);

        l.add(b4);
        dbWriter.write(l);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> result = session.createQuery("From BinanceData").getResultList();
        assertTrue(result.get(0).getSymbol().equals(symbol));
        dropTables();
    }

}
