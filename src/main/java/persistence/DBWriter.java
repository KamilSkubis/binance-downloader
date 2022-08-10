package persistence;

import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class DBWriter {

    public static void writeSymbol(SessionFactory sessionFactory,Symbol symbol) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(symbol);
        transaction.commit();
        session.close();
    }

    public static void writeData(SessionFactory sessionFactory, Data d) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> symbolList = dbReader.getSymbolObjFromDb(d.getSymbol().getSymbolName());
        if(symbolList.size() == 1){
            Symbol persistentSymbol = session.get(Symbol.class,symbolList.get(0).getId());
            d.setSymbol(persistentSymbol);
        }
        session.save(d);
        transaction.commit();
        session.close();
    }

}
