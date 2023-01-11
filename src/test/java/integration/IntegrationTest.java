package integration;

import config.Config;
import downloads.Downloader;
import model.BinanceData;
import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
        Downloader downloader = new DownloaderStub();

        Writer writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        Reader reader = new DbReader(MySQLUtilTesting.getSessionFactory());
        BatchWriter batchWriter = new BatchWriter(config);

    }


}

class DownloaderStub implements Downloader {

    @Override
    public List<String> getTickers() {
        return List.of("testUSDT", "wrongTicker");
    }

    @Override
    public List<Data> downloadKlines(LinkedHashMap<String, Object> params, List<Symbol> symbols) {
        return generateData(2);
    }


    private List<Data> generateData(int number) {
        List<Data> data = new ArrayList<>();
        Symbol symbol = new Symbol();
        symbol.setSymbolName("testUSDT");

        LocalDateTime now = LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20);
        for (int i = 0; i < number; i++) {
            var d = new BinanceData();
            d.setSymbol(symbol);
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