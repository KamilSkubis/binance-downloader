package persistence;


import config.FileConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class SessionFactoryCreator {

    private final SessionFactory sessionFactory;

    public SessionFactoryCreator(FileConfig fileConfig) {


        Properties properties = prepareCustomProperties(fileConfig);
        Configuration configuration = createConfigurationObj(properties);
        sessionFactory = createSessionFactory(properties, configuration);
    }

    private SessionFactory createSessionFactory(Properties properties, Configuration configuration) {
        final SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(properties).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    @NotNull
    private Configuration createConfigurationObj(Properties properties) {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addProperties(properties);
        configuration.addAnnotatedClass(model.Symbol.class);
        configuration.addAnnotatedClass(model.BinanceData.class);
        return configuration;
    }

    @NotNull
    private Properties prepareCustomProperties(FileConfig fileConfig) {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", fileConfig.getUrl());
        properties.put("hibernate.connection.username", fileConfig.getLogin());
        properties.put("hibernate.connection.password", fileConfig.getPassword());
        return properties;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
