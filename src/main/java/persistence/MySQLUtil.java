package persistence;

import config.ConfigLocation;
import config.ConfigReader;
import config.FileConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class MySQLUtil {
    private static SessionFactory sessionFactory;
    private static Logger logger;
    private static FileConfig fileConfig;

    private MySQLUtil() {
    }

    public static SessionFactory getSessionFactory() {

        if (logger == null) {
            logger = LoggerFactory.getLogger(MySQLUtil.class);
        }

        if (sessionFactory == null) {
            logger.debug("SessionFactory is null. Need to create new one");
            ConfigLocation configLocation = new ConfigLocation();
            ConfigReader configReader = new ConfigReader();
            fileConfig = configReader.read(configLocation);
            buildSessionFactory();
        }
        return sessionFactory;
    }


    private static SessionFactory buildSessionFactory() {
        try {

            Configuration configuration = new Configuration();
            configuration.configure();

            Properties properties = new Properties();
            properties.put("hibernate.connection.url", fileConfig.getUrl());
            properties.put("hibernate.connection.username", fileConfig.getLogin());
            properties.put("hibernate.connection.password", fileConfig.getPassword());
            properties.put("hibernate.hbm2ddl.auto", "update");
            configuration.addProperties(properties);
            configuration.addAnnotatedClass(model.Symbol.class);
            configuration.addAnnotatedClass(model.BinanceData.class);

            ServiceRegistry serviceRegistry;
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(properties).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            logger.debug("SessionFactory created.");

            return sessionFactory;

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            logger.warn(ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }


}

