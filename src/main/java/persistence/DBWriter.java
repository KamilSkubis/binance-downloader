package persistence;

import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


public class DBWriter {

    public static void writeSymbol(SessionFactory sessionFactory,Symbol symbol) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        session.save(symbol);
        transaction.commit();
        session.close();
    }

    public static void writeData(SessionFactory sessionFactory,Data d) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        session.save(d);
        transaction.commit();
        session.close();
    }
}
