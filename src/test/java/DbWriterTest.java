import model.Binance1d;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.DBWriter;
import persistence.MySQLUtilTesting;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DbWriterTest {

    @Before
    public void setUp() {
        UtilForTesting.createTables();
    }

    @Test
    public void binance_1d_shouldBeEmpty() {
        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        Query query = session.createQuery("from Binance1d");
        assertEquals(0, query.getResultList().size());
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
        Binance1d binance1d = UtilForTesting.createSampleData(symbol);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
                .createQuery("From Binance1d")
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
        Binance1d binance1d1 = UtilForTesting.createSampleData(symbol);
        Binance1d binance1d2 = UtilForTesting.createSampleData(symbol);

        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
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
        Binance1d binance1d1 = UtilForTesting.createSampleData(symbol);
        Binance1d binance1d2 = UtilForTesting.createSampleData(symbol);

        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
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

        Binance1d binance1d1 = UtilForTesting.createSampleData(symbol1);
        Binance1d binance1d2 = UtilForTesting.createSampleData(symbol2);

        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
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

        Binance1d binance1d1 = UtilForTesting.createSampleData(symbol1);
        Binance1d binance1d2 = UtilForTesting.createSampleData(symbol3);
        Binance1d binance1d3 = UtilForTesting.createSampleData(symbol2);

        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d2);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d3);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result = resultList.size();
        System.out.println("Three inserts two separate tickers: resultList: " + resultList);
        session.close();
        assertEquals(2, result);
    }

    @Test
    public void canWriteSymbol(){
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testTwoEntry");
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testTwoEntry");
        Symbol symbol3 = new Symbol();
        symbol3.setSymbolName("testThreeEntry");


        DBWriter.writeSymbol(MySQLUtilTesting.getSessionFactory(),symbol1);
        DBWriter.writeSymbol(MySQLUtilTesting.getSessionFactory(),symbol2);
        DBWriter.writeSymbol(MySQLUtilTesting.getSessionFactory(),symbol3);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
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
