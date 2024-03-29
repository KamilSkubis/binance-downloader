package persistence;

import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class DbWriter implements Writer {

    private final SessionFactory sessionFactory;
    private final Logger logger;

    public DbWriter(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        logger = LoggerFactory.getLogger(DbWriter.class);
    }

    public DbWriter() {
        this.sessionFactory = MySQLUtil.getSessionFactory();
        logger = LoggerFactory.getLogger(DbWriter.class);
    }


    @Override
    public void write(Data data) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(data.getSymbol());
        session.save(data);
        transaction.commit();
        session.close();
    }


    @Override
    public void write(List<Data> data) {
        logger.info("Saving data to db for: " + data.get(0).getSymbol().getSymbolName());
        long now = System.nanoTime();
        writeDataUsingHibernate(data);
        long elapsed = (System.nanoTime() - now) / 1_000_000;


        logger.info("Saving all data "
                + data.size() + " for "
                + data.get(0).getSymbol().getSymbolName()
                + " to Database took: " + elapsed + " ms");
    }

    private void writeDataUsingHibernate(List<Data> data) {
        int i = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        for (Data d : data) {
            session.save(d);
            i++;
            if (i % 50 == 0) {
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
        session.close();
    }

    @Override
    public void write(Symbol symbol) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(symbol);
        transaction.commit();
        session.close();
    }

}
