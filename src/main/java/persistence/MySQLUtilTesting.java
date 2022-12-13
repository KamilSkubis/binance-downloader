package persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySQLUtilTesting {
    private static SessionFactory sessionFactory = null;

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("mysql_testing.cfg.xml")
                    .buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null){
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}

