import model.BinanceData;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.DbWriter;
import persistence.MySQLUtilTesting;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DbWriterTest {

    @Before
    public void setUp() {
        UtilForTesting.dropTables();
//        UtilForTesting.createTables();
    }

    @Test
    public void symbol_shouldBeEmpty() {
        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        Query query = session.createQuery("from Symbol");
        assertEquals(0, query.getResultList().size());

    }

    @Test
    public void canAddDataToDb_oneTime() {
        Symbol symbol = new Symbol();
        symbol.setSymbolName("testSingleEntry");
        BinanceData binanceData = UtilForTesting.createSampleData(symbol);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binanceData);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> resultList = session
                .createQuery("From BinanceData")
                .getResultList();
        int result = (int) resultList.stream()
                .filter(d -> d.getSymbol().getSymbolName().equals("testSingleEntry"))
                .count();
        session.close();
        assertEquals(1, result);
    }

    @Test
    public void canAddDataToDb_twoTimes_shouldHaveOneSymbol() {
        Symbol symbol = new Symbol();
        symbol.setSymbolName("testTwoEntry");
        BinanceData binance1d1 = UtilForTesting.createSampleData(symbol);
        BinanceData binanceData2 = UtilForTesting.createSampleData(symbol);

        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binance1d1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binanceData2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result = resultList.size();
        session.close();
        assertEquals(1, result);
    }


    @Test
    public void canAddData_twoSessions_sameSymbolObj_shouldHaveOneSymbol() {
        Symbol symbol = new Symbol();
        symbol.setSymbolName("testTwoEntry");
        BinanceData binance1d1 = UtilForTesting.createSampleData(symbol);
        BinanceData binanceData2 = UtilForTesting.createSampleData(symbol);

        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binance1d1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binanceData2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result = resultList.size();
        System.out.println(resultList);
        session.close();
        assertEquals(1, result);
    }

    @Test
    public void canAddData_twoSessions_differentSymbolObj_shouldHaveOneSymbol() {
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testTwoEntry");
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testTwoEntry");

        BinanceData binance1d1 = UtilForTesting.createSampleData(symbol1);
        BinanceData binanceData2 = UtilForTesting.createSampleData(symbol2);

        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binance1d1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binanceData2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result = resultList.size();
        System.out.println(resultList);
        session.close();
        assertEquals(1, result);
    }


    @Test
    public void canAddData_threeSessions_differentSymbolObj_shouldHaveTwoSymbol() {
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testTwoEntry");
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testTwoEntry");
        Symbol symbol3 = new Symbol();
        symbol3.setSymbolName("testThreeEntry");

        BinanceData binance1d1 = UtilForTesting.createSampleData(symbol1);
        BinanceData binanceData2 = UtilForTesting.createSampleData(symbol3);
        BinanceData binanceData3 = UtilForTesting.createSampleData(symbol2);

        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binance1d1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binanceData2);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(binanceData3);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result = resultList.size();
        System.out.println("Three inserts two separate tickers: resultList: " + resultList);
        session.close();
        assertEquals(2, result);
    }

    @Test
    public void canWriteSymbol() {
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testTwoEntry");
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testTwoEntry");
        Symbol symbol3 = new Symbol();
        symbol3.setSymbolName("testThreeEntry");


        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeSymbol(symbol1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeSymbol(symbol2);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeSymbol(symbol3);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<BinanceData> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result = resultList.size();
        session.close();
        assertEquals(3, result);
    }


    @After
    public void tearDown() {
        UtilForTesting.dropTables();
    }

}
