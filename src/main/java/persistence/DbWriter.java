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

//    public void writeSymbol(Symbol symbol) {
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//        session.save(symbol);
//        transaction.commit();
//        session.close();
//    }
//
//    public void writeData(Data d) {
//        Session session = sessionFactory.openSession();
//        Transaction transaction = sessionFactory.openSession().beginTransaction();
//
//        DbReader dbReader = new DbReader(sessionFactory);
//        List<Symbol> symbolList = dbReader.getSymbolObjFromDb(d.getSymbol().getSymbolName());
//        if(symbolList.size() == 1){
//            Symbol persistentSymbol = session.get(Symbol.class,symbolList.get(0).getId());
//            d.setSymbol(persistentSymbol);
//        }
//        session.save(d);
//        transaction.commit();
//        session.close();
//    }

//    public void writeDatainBatch(List<Data> data) {
//
//        Logger logger = LoggerFactory.getLogger(DbWriter.class);
//
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        DbReader dbReader = new DbReader(sessionFactory);
//        logger.info("reading last index value for ticker: " + data.get(0).getSymbol().getSymbolName());
//        long index;
//        try{
//        index = dbReader.getLatestIndex();
//        logger.info("last index value: " + index);
//        index++;
//        logger.info("incrementing index value by 1:  " + index);
//        }catch (NoResultException e){
//            logger.info("found no tickier on this name: " + data.get(0).getSymbol().getSymbolName()
//            + " setting index to 1");
//            index =1;
//        }
//
//        Symbol persistentSymbol = data.get(0).getSymbol();
//        List<Symbol> symbolList = dbReader.getSymbolObjFromDb(data.get(0).getSymbol().getSymbolName());
//        if(symbolList.size() != 0) {
//            persistentSymbol = session.get(Symbol.class, symbolList.get(0).getId());
//        }
//
//        Long startTime = System.currentTimeMillis();
//
//        for (Data d : data) {
//            d.setSymbol(persistentSymbol);
//            d.setId(index);
//            session.save(d);
//
//            if(index % 50 == 0){
//                session.flush();
//                session.clear();
//            }
//            index++;
//        }
//
//        transaction.commit();
//        session.close();
//
//        Long endTime = System.currentTimeMillis();
//        long duration = (endTime - startTime) /1000;
//        logger.info("Saved to database: " + data.size() + " records for: " + data.get(0).getSymbol().getSymbolName() + " ticker, this operation took: " + duration);
//    }

    @Override
    public void write(Data data) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(data);
        transaction.commit();
        session.close();
    }


    @Override
    public void write(List<Data> data) {
        Long now = System.nanoTime();

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

        Long elapsed = (System.nanoTime() - now) / 1_000_000;
        logger.info("Saving all data for " + data.get(0).getSymbol().getSymbolName() + " to Database took: " + elapsed + " ms");

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
