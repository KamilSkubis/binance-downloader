package persistence;

import config.ConfigReader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.ServiceBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class MySQLUtil {
    private static SessionFactory sessionFactory;
    private static Logger logger;

    private MySQLUtil(){};

    public static SessionFactory getSessionFactory() {

        if(logger == null){
            logger = LoggerFactory.getLogger(MySQLUtil.class);
        }

        if(sessionFactory == null){
             buildSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            ConfigReader configReader = new ConfigReader();
            if(configReader.userSettingsExists()){

                String url = configReader.getUrl();
                String login = configReader.getLogin();
                String password = configReader.getPassword();

                Configuration configuration = new Configuration();
                configuration.configure();

                Properties properties = new Properties();
                properties.put("hibernate.connection.url",url);
                properties.put("hibernate.connection.username",login);
                properties.put("hibernate.connection.password",password);
                configuration.addProperties(properties);
                configuration.addAnnotatedClass(model.Symbol.class);
                configuration.addAnnotatedClass(model.BinanceData.class);

                logger.info("Found user settings file:");
                logger.info("Modified configuration object to: " + configuration.getProperties().toString());

                ServiceRegistry serviceRegistry;
                serviceRegistry = new StandardServiceRegistryBuilder().applySettings(properties).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;
            }else {
                // Create the SessionFactory from hibernate.cfg.xml
                sessionFactory = new Configuration().configure().buildSessionFactory();
                return sessionFactory;
            }
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            logger.warn(ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public  void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }



}

