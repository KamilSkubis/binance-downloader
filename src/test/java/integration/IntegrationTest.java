package integration;

import Binance.BinanceRunner;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

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
        DataRepository dr = new DataRepository(writer, batchWriter, reader);

        BinanceRunner br = new BinanceRunner(dr, downloader, config);

        br.run();
        br.run();

        List<Data> data = reader.getData();
        assertEquals(4, data.size());
    }


}

class DownloaderStub implements Downloader {

    private LocalDateTime now;

    public DownloaderStub() {
        this.now = LocalDateTime.of(2000, 1, 1, 5, 25, 2, 20);
        ;
    }

    @Override
    public List<String> getTickers() {
        return List.of("testUSDT", "wrongTicker");
    }

    @Override
    public List<Data> downloadKlines(LinkedHashMap<String, Object> params, List<Symbol> symbols) {
        return generateData(2, symbols, "testUSDT");
    }


    private List<Data> generateData(int number, List<Symbol> symbols, String ticker) {
        List<Data> data = new ArrayList<>();
        System.out.println(symbols.toString());

        var symbol = symbols.stream().filter(s -> s.getSymbolName().equals(ticker)).collect(Collectors.toList()).get(0);

        for (int i = 0; i < number; i++) {
            var d = new BinanceData();
            d.setOpenTime(now);
            d.setVolume(Math.random());
            d.setSymbol(symbol);
            d.setOpen(Math.random());
            d.setHigh(Math.random());
            d.setLow(Math.random());
            d.setClose(Math.random());
            now = now.plusMinutes(1);
            data.add(d);
        }
        data.stream().forEach(d -> System.out.println(d.toString() + " " + d.getDataId().toString()));
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