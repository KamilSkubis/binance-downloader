import downloads.BinanceDownloader;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import persistence.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SymbolSynchronizatorTest {

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

        if (tables.contains("binance_data_seq")) {
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
    public void shouldSynchronizeSymbolsWithPersistence() {

        SessionFactory ss = MySQLUtilTesting.getSessionFactory();
        Writer writer = new DbWriter(ss);
        Reader reader = new DbReader(ss);
        DataRepository dataRepository = new DataRepository(writer, reader);

        BinanceDownloader downloader = mock(BinanceDownloader.class);
        when(downloader.getTickers()).thenReturn(List.of("test1USDT", "test2USDT"));
        List<String> downloadedSymbols = downloader.getTickers();

        Symbol s1 = new Symbol("test1USDT");
        writer.write(s1);

        List<Symbol> symbols = dataRepository.getSymbols();

        SymbolSynchronizator symbolSynchronizator = new SymbolSynchronizator(writer, reader);
        symbolSynchronizator.synchronizeUSDTSymbols(symbols, downloadedSymbols);

        List<Symbol> symbolListAfterSynchronization = dataRepository.getSymbols();
        symbolListAfterSynchronization.forEach(System.out::println);

        Set<String> set = symbolListAfterSynchronization.stream().map(Symbol::getSymbolName).collect(Collectors.toSet());

        assertEquals(2, symbolListAfterSynchronization.size());
        assertTrue(set.contains("test1USDT"));
        assertTrue(set.contains("test2USDT"));

    }
}
