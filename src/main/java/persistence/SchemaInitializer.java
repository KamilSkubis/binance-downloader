package persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaInitializer {

    private final SessionFactory sessionFactory;
    private final Logger logger;

    public SchemaInitializer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.logger = LoggerFactory.getLogger(SchemaInitializer.class);
    }

    public void initializeSchemasOrDoNothing() {

        Session s0 = sessionFactory.openSession();
        final var tables = s0.createSQLQuery("Show tables")
                .getResultList();
        s0.close();


        if (tables.isEmpty()) {
            Session s1 = sessionFactory.openSession();
            logger.debug("No tables in database, need to create one");
            Transaction transaction1 = s1.beginTransaction();

            String binanceData = "create table binance_data(id bigint ,symbol_id int,open_time datetime(6),open double,high double,low double,close double,volume double,key(id));";
            s1.createSQLQuery(binanceData).executeUpdate();
            transaction1.commit();
            s1.close();

            Session s2 = sessionFactory.openSession();
            Transaction transaction2 = s2.beginTransaction();
            String symbol = "create table symbols(id bigint AUTO_INCREMENT,symbol char(15),key(id));";
            s2.createSQLQuery(symbol).executeUpdate();
            transaction2.commit();
            s2.close();
            logger.debug("tables created");

            Session s3 = sessionFactory.openSession();
            var t3 = s3.beginTransaction();
            s3.createSQLQuery("create table xxx(id bigint AUTO_INCREMENT,symbol char(10),key(id));").executeUpdate();
            t3.commit();
            s3.close();


        }
    }
}
