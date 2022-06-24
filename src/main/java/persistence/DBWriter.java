package persistence;

import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

public class DBWriter {

    private final Session session;

    public DBWriter(Session session) {
        this.session = session;
    }

    @Transactional
    public void writeSymbol(Symbol symbol) {

        Transaction transaction = session.getTransaction();
        session.save(symbol);
        transaction.commit();
    }

    @Transactional
    public void writeData(Data d) {
        Transaction transaction = session.getTransaction();
        session.save(d);
        transaction.commit();
    }
}
