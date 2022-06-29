import model.Binance1d;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import persistence.DBWriter;
import persistence.MySQLUtilTesting;

import java.util.List;

import static org.junit.Assert.*;

public class SaveToDatabaseTest {

    @Before
    public void setUp(){
        Session session =(Session) MySQLUtilTesting.getSessionFactory().openSession();

        session.beginTransaction();
        String binance = "create table binance_1d(\n" +
                "        id bigint AUTO_INCREMENT,\n" +
                "        symbol_id int,\n" +
                "        open_time bigint signed,\n" +
                "        open double,\n" +
                "        high double,\n" +
                "        low double,\n" +
                "        close double,\n" +
                "        volume double,\n" +
                "        key(id)\n" +
                "        );";

        String symbol = "create table symbols(\n" +
                "id bigint AUTO_INCREMENT,\n" +
                "symbol char(15),\n" +
                "key(id)\n" +
                ");";

        session.createSQLQuery(symbol).executeUpdate();
        session.createSQLQuery(binance).executeUpdate();
        session.getTransaction().commit();
        session.close();

    }

    @Test
    public void binance_1d_shouldBeEmpty(){
        Session session = (Session) MySQLUtilTesting.getSessionFactory().openSession();
        Query query = session.createQuery("from Binance1d");
        assertEquals(0,query.getResultList().size());
    }

    @Test
    public void symbol_shouldBeEmpty(){
        Session session = (Session) MySQLUtilTesting.getSessionFactory().openSession();
        Query query = session.createQuery("from Symbol");
        assertEquals(0,query.getResultList().size());

    }

    @Test
    public void canAddDataToDb_oneTime(){
        Symbol symbol = new Symbol();
        symbol.setSymbol("testSingleEntry");
        Binance1d binance1d = createSampleData(symbol);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
                .createQuery("From Binance1d")
                .getResultList();
        int result = (int) resultList.stream()
                .filter(d -> d.getSymbol().getSymbol().equals("testSingleEntry"))
                .count();
        session.close();
        assertEquals(1,result);
    }

    @Test
    public void canAddDataToDb_twoTimes_shouldHaveOneSymbol(){
        Symbol symbol = new Symbol();
        symbol.setSymbol("testTwoEntry");
        Binance1d binance1d1 = createSampleData(symbol);
        Binance1d binance1d2 = createSampleData(symbol);

        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result =resultList.size();
        session.close();
        assertEquals(1,result);
    }


    @Test
    public void canAddData_twoSessions_shouldHaveOneSymbol(){
        Symbol symbol = new Symbol();
        symbol.setSymbol("testTwoEntry");
        Binance1d binance1d1 = createSampleData(symbol);
        Binance1d binance1d2 = createSampleData(symbol);

        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d1);
        DBWriter.writeData(MySQLUtilTesting.getSessionFactory(), binance1d2);

        Session session = MySQLUtilTesting.getSessionFactory().openSession();
        List<Binance1d> resultList = session
                .createQuery("From Symbol")
                .getResultList();
        int result =resultList.size();
        System.out.println(resultList);
        session.close();
        assertEquals(1,result);
    }






    @NotNull
    private Binance1d createSampleData(Symbol symbol) {
        String symbolName = symbol.getSymbol();
        symbol.setSymbol(symbolName);
        Binance1d binance1d = new Binance1d();
        binance1d.setOpenTime(1203l);
        binance1d.setVolume(230.2);
        binance1d.setSymbol(symbol);
        binance1d.setOpen(323.41);
        binance1d.setHigh(23132.1);
        binance1d.setLow(231.3);
        binance1d.setClose(95.21);
        return binance1d;
    }


    @After
    public void tearDown(){
        Session session = (Session) MySQLUtilTesting.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DROP TABLE symbols").executeUpdate();
        session.createSQLQuery("DROP TABLE binance_1d").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

}
