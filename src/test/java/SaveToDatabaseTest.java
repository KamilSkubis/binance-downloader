import model.Symbol;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.*;
import persistence.MySQLTesting;

import java.util.List;

import static org.junit.Assert.*;

public class SaveToDatabaseTest {
    Session session;

    @Before
    public void setUp(){
        session = (Session) MySQLTesting.getSessionFactory().openSession();

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
    }

    @Test
    public void binance_1d_shouldBeEmpty(){
        Query query = session.createQuery("from Data");
        assertEquals(0,query.getResultList().size());

    }

    @Test
    public void symbol_shouldBeEmpty(){
        Query query = session.createQuery("from Symbol");
        assertEquals(0,query.getResultList().size());

    }

    @Test
    public void canAdd_OneDataToDb(){
        Symbol symbol = new Symbol();
        symbol.setSymbol("test");
        Transaction t = session.beginTransaction();
        session.save(symbol);
        t.commit();

        List<Symbol> symbolList = session.createQuery("from Symbol").getResultList();
        assertEquals(1,symbolList.size());

    }


    @After
    public void tearDown(){
        session.beginTransaction();
        session.createSQLQuery("DROP TABLE symbols").executeUpdate();
        session.createSQLQuery("DROP TABLE binance_1d").executeUpdate();
        session.getTransaction().commit();
    }

}
