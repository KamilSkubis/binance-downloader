package persistence;

import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.List;


public class DBWriter {

    public static void writeSymbol(SessionFactory sessionFactory,Symbol symbol) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(symbol);
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

    public static void writeDatainBatch(SessionFactory sessionFactory, List<Data> data) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        DbReader dbReader = new DbReader(sessionFactory);


        long index = 0;
        try{
        index = dbReader.getLatestIndex();
        index++;
        }catch (NoResultException e){
            index =0;
        }

        Symbol persistentSymbol = data.get(0).getSymbol();
        List<Symbol> symbolList = dbReader.getSymbolObjFromDb(data.get(0).getSymbol().getSymbolName());
        if(symbolList.size() != 0) {
            persistentSymbol = session.get(Symbol.class, symbolList.get(0).getId());
        }

        for (Data d : data) {
            d.setSymbol(persistentSymbol);
            d.setId(index);
            session.save(d);

            if(index % 50 == 0){
                session.flush();
                session.clear();
            }
            index++;
        }

        transaction.commit();
        session.close();

        Logger logger = LoggerFactory.getLogger(DBWriter.class);
        logger.info("Saved to database: " + data.size() + " records for: " + data.get(0).getSymbol().getSymbolName() + " ticker");
    }
}
