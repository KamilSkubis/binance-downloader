import model.Binance1d;
import model.Symbol;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import persistence.MySQLUtilTesting;

public class UtilForTesting {

    public static void createTables(){
        Session session =(Session) MySQLUtilTesting.getSessionFactory().openSession();

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
    public static Binance1d createSampleData(Symbol symbol) {
        String symbolName = symbol.getSymbolName();
        symbol.setSymbolName(symbolName);
        Binance1d binance1d = new Binance1d();
        binance1d.setOpenTime(21000l);
        binance1d.setVolume(230.2);
        binance1d.setSymbol(symbol);
        binance1d.setOpen(323.41);
        binance1d.setHigh(23132.1);
        binance1d.setLow(231.3);
        binance1d.setClose(95.21);
        return binance1d;
    }
}
