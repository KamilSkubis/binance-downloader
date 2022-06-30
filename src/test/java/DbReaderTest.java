import com.mysql.cj.util.TestUtils;
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

public class DbReaderTest {

    @Before
    public void setUp(){
        UtilForTesting.createTables();
    }

    @Test
    public void returnDateDiffrence_betweenDateDbAndFromDate(){
        SessionFactory mysqlTesting = MySQLUtilTesting.getSessionFactory();
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testRead");
        Binance1d b1= UtilForTesting.createSampleData(symbol1);
        DBWriter.writeData(mysqlTesting, b1);

        DbReader dbReader = new DbReader(mysqlTesting);
        Long askDate = 23000l;
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testRead");
        Assert.assertEquals(2000, dbReader.readLastDate(symbol2,askDate));
    }

    @After
    public void tearDown(){
        UtilForTesting.dropTables();
    }
}
