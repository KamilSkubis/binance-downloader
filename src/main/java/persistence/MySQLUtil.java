package persistence;

import config.ConfigReader;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

                return new Configuration()
                        .configure()
                        .setProperty("connection.url",url)
                        .setProperty("connection.username",login)
                        .setProperty("connection.password",password)
                        .buildSessionFactory();
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

