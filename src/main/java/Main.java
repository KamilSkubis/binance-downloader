import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import config.Config;
import config.ConfigLocation;
import config.ConfigReader;
import downloads.BinanceDownloader;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.*;


public class Main {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);


        SessionFactory sessionFactory = MySQLUtil.getSessionFactory();

        SchemaInitializer schemaInitializer = new SchemaInitializer(sessionFactory);
        schemaInitializer.initializeSchemasOrDoNothing();

        Long startTime = System.currentTimeMillis();

        Config config = new ConfigReader().read(new ConfigLocation());
        DataRepository dataRepository = createDataRepository(sessionFactory, config);
        BinanceDownloader downloader = createBinanceDownloader();
        BinanceRunner binanceRunner = new BinanceRunner(dataRepository, downloader, config);
        binanceRunner.run();

        Long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Program took " + duration + "ms or " + duration / 1000 + "s");

        MySQLUtil.getSessionFactory().close();
    }

    @NotNull
    private static DataRepository createDataRepository(SessionFactory sql, Config config) {
        Writer writer = new DbWriter(sql);
        Reader reader = new DbReader(sql);
        BatchWriter batchWriter = new BatchWriter(config);
        return new DataRepository(writer, batchWriter, reader);
    }


    @NotNull
    private static BinanceDownloader createBinanceDownloader() {
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market = client.createMarket();
        return new BinanceDownloader(market);
    }

}
