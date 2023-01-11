import model.BinanceData;
import model.Symbol;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import persistence.MySQLUtilTesting;

import java.time.LocalDateTime;

public class UtilForTesting {

    public static void createTables(){
        Session session = MySQLUtilTesting.getSessionFactory().openSession();

        session.beginTransaction();
//        String binance = "create table binance_data(\n" +
//                "        id bigint AUTO_INCREMENT,\n" +
//                "        symbol_id int,\n" +
//                "        open_time datetime(6),\n" +
//                "        open double,\n" +
//                "        high double,\n" +
//                "        low double,\n" +
//                "        close double,\n" +
//                "        volume double,\n" +
//                "        key(id)\n" +
//                "        );";

        String binance = "create table binance_data(id bigint ,symbol_id int,open_time datetime(6),open double,high double,low double,close double,volume double,key(id));";


        String symbol = "create table symbols(id bigint AUTO_INCREMENT,symbol char(15),key(id));";



        session.createSQLQuery(symbol).executeUpdate();
        session.createSQLQuery(binance).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public static void dropTables() {
        Session session = MySQLUtilTesting.getSessionFactory().openSession();

        final var tables = session.createSQLQuery("Show tables")
                .getResultList();

        session.beginTransaction();
        if (tables.contains("binance_data")) {
            session.createSQLQuery("truncate TABLE binance_data").executeUpdate();
        }
        if (tables.contains("symbols")) {
            session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
            session.createSQLQuery("truncate TABLE symbols").executeUpdate();
            session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        }

        if(tables.contains("binance_data_seq")) {
            session.createSQLQuery("truncate binance_data_seq").executeUpdate();
            session.createSQLQuery("insert into test.binance_data_seq(next_val) values ( 0 )").executeUpdate();
        }
        session.getTransaction().commit();
        session.close();
    }

    @NotNull
    public static BinanceData createSampleData(Symbol symbol) {

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
