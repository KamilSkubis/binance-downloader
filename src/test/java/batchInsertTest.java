import model.BinanceData;
import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.MySQLUtilWithParams;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class batchInsertTest {

    SessionFactory mysqlTesting;
    List<Data> datas;

    @Before
    public void setUp() {

        UtilForTesting.createTables();
        mysqlTesting = MySQLUtilWithParams.getSessionFactory();

        Symbol s = new Symbol();
        s.setSymbolName("abc");

        long id = 1l;
        datas = new ArrayList<>();
        for (int i = 0; i <= 370_000; i++) {
            Data d = new BinanceData();
            d.setId(id);
            id++;
            d.setSymbol(s);
            d.setClose(2003.20);
            d.setHigh(20302.21);
            d.setLow(2023.23);
            d.setVolume(100.2);
            d.setOpenTime(LocalDateTime.of(2000, 1, 1, 1, 1, 1));
            datas.add(d);
        }
    }

    @Test
    public void batchInsert100k_preparedStatements() {

        Session session = MySQLUtilWithParams.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();


        int batch = 50;


        long start = System.currentTimeMillis() / 1000;

        System.out.println();

        for (int i = 0; i < datas.size(); i++) {
            session.save(datas.get(i));

            if (i % batch == 0) {
                session.flush();
                session.clear();
            }
        }


        transaction.commit();

        long end = System.currentTimeMillis() / 1000;
        long duration = end - start;
        System.out.println("Duration: " + duration + " s");
        session.close();

        assertTrue(duration < 120);
    }

//    @Test
//    public void testWithoutBatch(){
//
//
//
//
//        Session session = MySQLUtilTesting.getSessionFactory().openSession();
//        Transaction transaction = session.beginTransaction();
//
//        long start =System.currentTimeMillis()/1000;
//
//        for(Data d : datas) {
//            DBWriter.writeData(session.getSessionFactory(), d);
//        }
//
//        long end = System.currentTimeMillis()/1000;
//        long duration = end - start;
//        System.out.println("Duration: " + duration + " s");
//
//        assertTrue(duration < 120);
//    }
//
//
    @After
    public void tearDown() {
        UtilForTesting.dropTables();
    }

}
