import model.Symbol;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.DbWriter;
import persistence.DataManager;
import persistence.MySQLUtilTesting;


import java.util.ArrayList;
import java.util.List;

public class DataManagerTest {
    SessionFactory mysqlTesting;

    @Before
    public void setUp() {
        UtilForTesting.dropTables();
        mysqlTesting = MySQLUtilTesting.getSessionFactory();
    }

    @Test
    public void canReadSymbol_returnListOfSymbolObj_symbolListIsEmpty() {
        writeSymbolsToDb();

        List<String> symbolList = new ArrayList<>();

        DataManager dataManager = new DataManager(MySQLUtilTesting.getSessionFactory());
        dataManager.saveOrUpdateSymbols(symbolList);

        List<Symbol> readResult = dataManager.getSymbolList();

        Assert.assertEquals(2, readResult.size());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test1")).count());
    }


    @Test
    public void canReadSymbol_returnListOfSymbolObj_symbolListEqualsSymbolsDb() {
        List<String> symbolList = List.of("test", "test1");
        writeSymbolsToDb();

        DataManager dataManager = new DataManager(MySQLUtilTesting.getSessionFactory());
        dataManager.saveOrUpdateSymbols(symbolList);

        List<Symbol> readResult = dataManager.getSymbolList();
        Assert.assertEquals(readResult.size(), symbolList.size());
        Assert.assertEquals(2, readResult.size());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test1")).count());
    }

    @Test
    public void canReadSymbol_returnListOfSymbolObj_symbolListBiggerThanSymbolsDb() {
        List<String> symbolList = List.of("test", "test1", "test2");
        writeSymbolsToDb();

        DataManager dataManager = new DataManager(MySQLUtilTesting.getSessionFactory());
        dataManager.saveOrUpdateSymbols(symbolList);
        List<Symbol> readResult = dataManager.getSymbolList();

        Assert.assertEquals(readResult.size(), symbolList.size());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test1")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test2")).count());
    }

//    @Test
//    public void returnCurrentListOfSymbolsToPersist_symbolListSmaller_returnSmallerList(){
//        List<String> symbolList = List.of("test");
//
//        writeSymbolsToDb();
//
//        DataManager dataManager = new DataManager(MySQLUtilTesting.getSessionFactory());
//        List<Symbol> readResult = dataManager.getSymbolsToDownload(symbolList);
//    }

    private void writeSymbolsToDb() {
        List<Symbol> symbolList = new ArrayList<>();
        Symbol symbol = new Symbol();
        symbol.setId(1L);
        symbol.setSymbolName("test");

        Symbol symbol1 = new Symbol();
        symbol.setId(2L);
        symbol1.setSymbolName("test1");

        new DbWriter(MySQLUtilTesting.getSessionFactory()).write(symbol);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).write(symbol1);

        DataManager dataManager = new DataManager(MySQLUtilTesting.getSessionFactory());
        Assert.assertEquals(2,dataManager.getSymbolList().size());
    }

    @After
    public void tearDown() {
        UtilForTesting.dropTables();
    }
}
