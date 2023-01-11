package persistence;

import config.Config;
import model.BinanceData;
import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BatchWriterTest {

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
    public void write_1000k_records() {
        var data = generateDataWithSymbolIdSetTo1();
        Config config = mock(Config.class);
        when(config.getUrl()).thenReturn("jdbc:mysql://localhost:3306/test");
        when(config.getLogin()).thenReturn("root");
        when(config.getPassword()).thenReturn("password");


        BatchWriter bw = new BatchWriter(config);
        bw.write(data);

    }

    private List<Data> generateDataWithSymbolIdSetTo1() {
        List<Data> data = new ArrayList<>();
        Symbol symbol = new Symbol();
        symbol.setSymbolName("testUSDT");
        DbWriter writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        writer.write(symbol);

        DbReader dbReader = new DbReader(MySQLUtilTesting.getSessionFactory());
        List testUSDT = dbReader.getSymbolByName("testUSDT");
        var persistentSymbol = (Symbol) testUSDT.get(0);


        LocalDateTime now = LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20);
        for (int i = 0; i < 1_000_000; i++) {
            var d = new BinanceData();
            d.setSymbol(persistentSymbol);
            now = now.plusMinutes(1);
            d.setOpenTime(now);
            d.setVolume(Math.random());
            d.setSymbol(symbol);
            d.setOpen(Math.random());
            d.setHigh(Math.random());
            d.setLow(Math.random());
            d.setClose(Math.random());
            data.add(d);
        }
        return data;
    }
}
