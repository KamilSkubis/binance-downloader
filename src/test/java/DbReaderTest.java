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

    @Test
    public void canReadSymbol_returnListOfSymbolObj_symbolListIsEmpty(){
        writeSymbolsToDb();

        DbReader dbReader = new DbReader(MySQLUtilTesting.getSessionFactory());
        List<String> symbolList = new ArrayList<>();

        List<Symbol> readResult = dbReader.getSymbolObjListFromDb(symbolList);

        Assert.assertEquals(2, readResult.size());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test1")).count());
    }



    @Test
    public void canReadSymbol_returnListOfSymbolObj_symbolListEqualsSymbolsDb(){
        List<String> symbolList = List.of("test","test1");
        writeSymbolsToDb();

        DbReader dbReader = new DbReader(MySQLUtilTesting.getSessionFactory());
        List<Symbol> readResult = dbReader.getSymbolObjListFromDb(symbolList);

        Assert.assertEquals(true, readResult.size() == symbolList.size());
        Assert.assertEquals(2, readResult.size());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test1")).count());
    }




    @After
    public void tearDown(){
        UtilForTesting.dropTables();
    }


    private List<Symbol> writeSymbolsToDb() {
        List<Symbol> symbolList = new ArrayList<>();
        Symbol symbol = new Symbol();
        symbol.setSymbolName("test");

        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("test1");

        DBWriter.writeSymbol(MySQLUtilTesting.getSessionFactory(),symbol);
        DBWriter.writeSymbol(MySQLUtilTesting.getSessionFactory(),symbol1);

        return symbolList;
    }
}
