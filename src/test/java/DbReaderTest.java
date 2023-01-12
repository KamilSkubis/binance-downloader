import model.BinanceData;
import model.Symbol;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.DbReader;
import persistence.DbWriter;
import persistence.MySQLUtilTesting;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DbReaderTest {

    SessionFactory mysqlTesting;


    @Before
    public void setUp() {
        UtilForTesting.dropTables();
        mysqlTesting = MySQLUtilTesting.getSessionFactory();
    }

    @Test
    public void returnDateDiffrence_betweenDateDbAndFromDate() {
        Symbol symbol1 = new Symbol();
        symbol1.setSymbolName("testRead");
        BinanceData b1 = UtilForTesting.createSampleData(symbol1);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).write(b1);

        DbReader dbReader = new DbReader(mysqlTesting);
        Symbol symbol2 = new Symbol();
        symbol2.setSymbolName("testRead");
        assertEquals(LocalDateTime.of(2000, 1, 1, 5, 25, 2), dbReader.readLastDate(symbol2));
    }


    @Test
    public void getSymbols_shouldHave_Default_LastDateInReturnedSymbol() {
        Symbol symbol = new Symbol("test");
        var data = UtilForTesting.createSampleData(symbol);
        new DbWriter(MySQLUtilTesting.getSessionFactory()).write(data);

        DbReader dbReader = new DbReader(mysqlTesting);
        List<Symbol> symbols = dbReader.getSymbols();

        var expResult = LocalDateTime.of(2000, 1, 1, 5, 25, 2);
        assertEquals(expResult, symbols.get(0).getLastDate());
    }

    @Test
    public void getSymbols_shouldHave_Real_LastDateInReturnedSymbol() {
        Symbol symbol = new Symbol("test");
        Symbol symbolWithoutData = new Symbol("test3");
        var data = UtilForTesting.createSampleData(symbol);
        var data2 = UtilForTesting.createSampleData(symbol);
        var data3 = UtilForTesting.createSampleData(symbol);
        var data4 = UtilForTesting.createSampleData(new Symbol("test2"));
        var customDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        data.setOpenTime(customDate);
        data2.setOpenTime(customDate.plusDays(1));
        data3.setOpenTime(customDate.plusDays(2));
        data4.setOpenTime(customDate.plusDays(2));
        DbWriter dbWriter = new DbWriter(MySQLUtilTesting.getSessionFactory());
        dbWriter.write(symbolWithoutData);
        dbWriter.write(data);
        dbWriter.write(data2);
        dbWriter.write(data3);
        dbWriter.write(data4);


        DbReader dbReader = new DbReader(mysqlTesting);
        List<Symbol> symbols = dbReader.getSymbols();

        symbols.stream().forEach(s -> System.out.println(s.toString()));
        Symbol result = symbols.stream().filter(s -> s.getSymbolName().equals("test")).findFirst().orElseThrow();
        assertEquals(customDate.plusDays(2), result.getLastDate());
    }


    @After
    public void tearDown() {
//        UtilForTesting.dropTables();
    }


}
