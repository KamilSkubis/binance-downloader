import config.Config;
import config.ConfigLocation;
import config.ConfigReader;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.MySQLUtil;
import persistence.SchemaInitializer;


public class Main {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);

        ConfigLocation configLocation = new ConfigLocation();
        ConfigReader configReader = new ConfigReader();
        Config config = configReader.read(configLocation);

        SessionFactory sessionFactory = MySQLUtil.getSessionFactory();

        SchemaInitializer schemaInitializer = new SchemaInitializer(sessionFactory.openSession());
        schemaInitializer.initializeSchemasOrDoNothing();

        Long startTime = System.currentTimeMillis();

        BinanceRunner binanceRunner = new BinanceRunner();
        binanceRunner.run();

        Long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Program took " + duration + "ms or " + duration / 1000 + "s");
    }

}
