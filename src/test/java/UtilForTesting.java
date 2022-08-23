import model.BinanceData;
import model.Symbol;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import persistence.MySQLUtilTesting;

import java.time.LocalDateTime;

public class UtilForTesting {

    public static void createTables(){
        Session session =(Session) MySQLUtilTesting.getSessionFactory().openSession();

        session.beginTransaction();
        String binance = "create table binance_1d(\n" +
                "        id bigint AUTO_INCREMENT,\n" +
                "        symbol_id int,\n" +
                "        open_time datetime(6),\n" +
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

    public static void dropTables(){
        Session session = (Session) MySQLUtilTesting.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DROP TABLE symbols").executeUpdate();
        session.createSQLQuery("DROP TABLE binance_1d").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @NotNull
    public static BinanceData createSampleData(Symbol symbol) {
        String symbolName = symbol.getSymbolName();
        symbol.setSymbolName(symbolName);
        BinanceData binanceData = new BinanceData();
        binanceData.setOpenTime(LocalDateTime.of(2000,1,1,5,25,2,20));
        binanceData.setVolume(230.2);
        binanceData.setSymbol(symbol);
        binanceData.setOpen(323.41);
        binanceData.setHigh(23132.1);
        binanceData.setLow(231.3);
        binanceData.setClose(95.21);
        return binanceData;
    }
}
