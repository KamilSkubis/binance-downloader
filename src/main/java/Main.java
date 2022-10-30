import config.Config;
import config.ConfigLocation;
import config.ConfigReader;
import org.hibernate.SessionFactory;
import persistence.MySQLUtil;
import persistence.SchemaInitializer;

/*
binanceRunnner(config, sessionFactory);

Na liście wywołuję



 */

public class Main {

    public static void main(String[] args) {

        ConfigLocation configLocation = new ConfigLocation();
        ConfigReader configReader = new ConfigReader();
        Config config = configReader.read(configLocation);

        SessionFactory sessionFactory = MySQLUtil.getSessionFactory();

        SchemaInitializer schemaInitializer = new SchemaInitializer(sessionFactory.openSession());
        schemaInitializer.initializeSchemasOrDoNothing();

        BinanceRunner binanceRunner = new BinanceRunner();
        binanceRunner.run();
    }

}
