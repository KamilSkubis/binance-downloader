import model.BinanceData;
import model.Symbol;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.DataRepository;
import persistence.DbReader;
import persistence.DbWriter;
import persistence.MySQLUtilTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataRepositoryTest {
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
        var writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        var reader = new DbReader(MySQLUtilTesting.getSessionFactory());


        DataRepository dataManager = new DataRepository(writer, reader);
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

        var writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        var reader = new DbReader(MySQLUtilTesting.getSessionFactory());
        DataRepository dataManager = new DataRepository(writer, reader);
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

        var writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        var reader = new DbReader(MySQLUtilTesting.getSessionFactory());
        DataRepository dataManager = new DataRepository(writer, reader);
        dataManager.saveOrUpdateSymbols(symbolList);
        List<Symbol> readResult = dataManager.getSymbolList();

        Assert.assertEquals(readResult.size(), symbolList.size());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test1")).count());
        Assert.assertEquals(1, readResult.stream().filter(s -> s.getSymbolName().equals("test2")).count());
    }


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

        var writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        var reader = new DbReader(MySQLUtilTesting.getSessionFactory());
        DataRepository dataManager = new DataRepository(writer, reader);
        Assert.assertEquals(2, dataManager.getSymbolList().size());
    }


    @Test
    public void writeTwoDataObjWithTwoDiffrentSymbols_OneSymbolSaved() {
        var symbol1 = new Symbol();
        symbol1.setSymbolName("test1");

//        var symbol2 = new Symbol();
//        symbol1.setSymbolName("test1");

        BinanceData sampleData1 = UtilForTesting.createSampleData(symbol1);


        new DbWriter(MySQLUtilTesting.getSessionFactory()).write(sampleData1);

        var writer = new DbWriter(MySQLUtilTesting.getSessionFactory());
        var reader = new DbReader(MySQLUtilTesting.getSessionFactory());
        DataRepository dataRepository = new DataRepository(writer, reader);

        var symbolList = dataRepository.getSymbolList();

        var symbol2 = symbolList.stream().filter(symbol -> Objects.equals(symbol.getSymbolName(), "test1")).collect(Collectors.toList());
        BinanceData sampleData2 = UtilForTesting.createSampleData(symbol2.get(0));
        new DbWriter(MySQLUtilTesting.getSessionFactory()).write(sampleData2);

        System.out.println(dataRepository.getSymbolList().toString());
        Assert.assertEquals(1, dataRepository.getSymbolList().size());
    }


    @After
    public void tearDown() {
        UtilForTesting.dropTables();
    }
}
