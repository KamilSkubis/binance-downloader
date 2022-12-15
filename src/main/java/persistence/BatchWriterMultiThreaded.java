//package persistence;
//
//import model.Data;
//import model.Symbol;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.persistence.NoResultException;
//import java.util.List;
//
//public class BatchWriterMultiThreaded implements Runnable {
//
//    private final SessionFactory sessionFactory;
//    private final List<Data> data;
//    private final Logger logger;
//
//    public BatchWriterMultiThreaded(SessionFactory sessionFactory, List<Data> data) {
//
//        this.sessionFactory = sessionFactory;
//        this.data = data;
//        this.logger = LoggerFactory.getLogger(DbWriter.class);
//    }
//
//
//    @Override
//    public void run() {
//
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        DbReader dbReader = new DbReader(sessionFactory);
//        logger.info("Using separate thread " + Thread.currentThread().getName());
//        logger.info("reading last index value for ticker: " + data.get(0).getSymbol().getSymbolName());
//        long index;
//        try {
//            index = dbReader.getLatestIndex();
//            logger.info("last index value: " + index);
//            index++;
//            logger.info("incrementing index value by 1:  " + index);
//        } catch (NoResultException e) {
//            logger.info("found no ticker on this name: " + data.get(0).getSymbol().getSymbolName()
//                    + " setting index to 1");
//            index = 1;
//        }
//
//        Symbol persistentSymbol = data.get(0).getSymbol();
//        List<Symbol> symbolList = dbReader.getSymbolObjFromDb(data.get(0).getSymbol().getSymbolName());
//        if (symbolList.size() != 0) {
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
//            if (index % 10000 == 0) {
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
//        long duration = (endTime - startTime) / 1000;
//        logger.info("Saved to database: " + data.size() + " records for: " + data.get(0).getSymbol().getSymbolName() + " ticker, this operation took: " + duration);
//    }
//}
//
