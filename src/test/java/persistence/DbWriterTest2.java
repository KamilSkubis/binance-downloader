package persistence;

import model.BinanceData;
import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    @Test
    public void batchInsert_1000k_data_objects() {
        List<Data> data = new ArrayList<>();
        Symbol symbol = new Symbol();
        symbol.setSymbolName("test");
        LocalDateTime now = LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20);
        for (int i = 0; i < 1_000_000; i++) {
            var d = new BinanceData();
            d.setSymbol(symbol);
            d.setOpenTime(now.plusMinutes(1));
            d.setVolume(Math.random());
            d.setSymbol(symbol);
            d.setOpen(Math.random());
            d.setHigh(Math.random());
            d.setLow(Math.random());
            d.setClose(Math.random());
            data.add(d);
        }

        DbWriter dbWriter = new DbWriter(MySQLUtilTesting.getSessionFactory());
        dbWriter.write(data);
    }

    //This method will write 1000k data objects to db
    // in 6.23115 min or 373869ms - without rewriteBatchStatements
    // in 1.212733 min or 72764ms - with rewriteBatchStatements
    @Test
    public void batchInsert_1000k_objects_nativeSQL_with_same_batch_and_array() {
        List<Data> data = new ArrayList<>();
        Symbol symbol = new Symbol();
        symbol.setSymbolName("test");
        LocalDateTime now = LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20);
        for (int i = 0; i < 1_000_000; i++) {
            var d = new BinanceData();
            d.setSymbol(symbol);
            d.setOpenTime(now.plusMinutes(1));
            d.setVolume(Math.random());
            d.setSymbol(symbol);
            d.setOpen(Math.random());
            d.setHigh(Math.random());
            d.setLow(Math.random());
            d.setClose(Math.random());
            data.add(d);
        }
//        DbWriter dbWriter = new DbWriter(MySQLUtilTesting.getSessionFactory());
//        dbWriter.batchUsingNativeSQL(data);

        String path = "jdbc:mysql://localhost:3306/test";
        String path2 = path + "?rewriteBatchedStatements=true";
        String user = "root";
        String pass = "password";

        Connection con = null;
        try {
            con = DriverManager.getConnection(path2, user, pass);
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(
                    "Insert into test.binance_data(id,close,high,low,open,open_time,volume,symbol_id) values(?,?,?,?,?,?,?,?)");
            int i = 0;
            long id = 0;
            long idSymbol = 1;

            PreparedStatement preparedStatement = con.prepareStatement("insert into symbols values(?,?)");
            preparedStatement.setLong(1, idSymbol);
            preparedStatement.setString(2, symbol.getSymbolName());
            preparedStatement.executeUpdate();

            long start = System.currentTimeMillis();


            for (Data d : data) {
                id++;
                var formatedDate = d.getOpenTime().format(DateTimeFormatter.BASIC_ISO_DATE);

                ps.setLong(1, id);
                ps.setDouble(2, d.getClose());
                ps.setDouble(3, d.getHigh());
                ps.setDouble(4, d.getLow());
                ps.setDouble(5, d.getOpen());
                ps.setString(6, formatedDate);
                ps.setDouble(7, d.getVolume());
                ps.setLong(8, idSymbol);

                ps.addBatch();
                i++;
                if (i % 100000 == 0) {

                    ps.executeLargeBatch();
                    con.commit();
                }

                ps.clearParameters();
            }

            long elapsed = System.currentTimeMillis() - start;
            System.out.println("elapsed time: " + elapsed);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
