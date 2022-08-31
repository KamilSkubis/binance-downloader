package persistence;

import config.ConfigReader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.ServiceBinding;

import java.util.Properties;

public class MySQLUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    public  SessionFactory getSessionFactory() {
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
                configuration.setProperty("connection.url",url);
                configuration.setProperty("connection.username",login);
                configuration.setProperty("connection.password",password);

                return configuration.buildSessionFactory();
            }else {
                // Create the SessionFactory from hibernate.cfg.xml
                return new Configuration().configure().buildSessionFactory();
            }
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public  void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }



}

