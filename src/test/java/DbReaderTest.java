import model.BinanceData;
import model.Symbol;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.DbWriter;
import persistence.DbReader;
import persistence.MySQLUtilTesting;


import java.time.LocalDateTime;

public class DbReaderTest {

    SessionFactory mysqlTesting;


    @Before
    public void setUp() {
        UtilForTesting.createTables();
        mysqlTesting = MySQLUtilTesting.getSessionFactory();
    }

    @Test
    public void returnDateDiffrence_betweenDateDbAndFromDate() {
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testRead");
        BinanceData b1 = UtilForTesting.createSampleData(symbol1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(b1);

        DbReader dbReader = new DbReader(mysqlTesting);
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testRead");
        Assert.assertEquals(LocalDateTime.of(2000, 1, 1, 5, 25, 2), dbReader.readLastDate(symbol2));
    }

    @Test
    public void canGetlatestIndex() {
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testRead");
        BinanceData b1 = UtilForTesting.createSampleData(symbol1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).writeData(b1);
        DbReader dbReader = new DbReader(mysqlTesting);
        Assert.assertEquals(100l, dbReader.getLatestIndex());

    }


    @After
    public void tearDown() {
        UtilForTesting.dropTables();
    }


}
