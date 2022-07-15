import model.Binance1d;
import model.Symbol;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.DBWriter;
import persistence.DbReader;
import persistence.MySQLUtilTesting;

import java.util.ArrayList;
import java.util.List;

public class DbReaderTest {

    SessionFactory mysqlTesting;


    @Before
    public void setUp(){
        UtilForTesting.createTables();
        mysqlTesting = MySQLUtilTesting.getSessionFactory();
    }

    @Test
    public void returnDateDiffrence_betweenDateDbAndFromDate(){
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testRead");
        Binance1d b1= UtilForTesting.createSampleData(symbol1);
        DBWriter.writeData(mysqlTesting, b1);

        DbReader dbReader = new DbReader(mysqlTesting);
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testRead");
        Assert.assertEquals(21000, dbReader.readLastDate(symbol2));
    }


    @After
    public void tearDown(){
        UtilForTesting.dropTables();
    }



}
