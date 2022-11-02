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
        UtilForTesting.createTables();
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
        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binanceData);

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

        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binanceData2);

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

        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binanceData2);

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

        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binanceData2);

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

        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binanceData2);
        new DbWriter().writeData(MySQLUtilTesting.getSessionFactory(), binanceData3);

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


        new DbWriter().writeSymbol(MySQLUtilTesting.getSessionFactory(), symbol1);
        new DbWriter().writeSymbol(MySQLUtilTesting.getSessionFactory(), symbol2);
        new DbWriter().writeSymbol(MySQLUtilTesting.getSessionFactory(), symbol3);

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
