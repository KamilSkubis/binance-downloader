package persistence;

import model.BinanceData;
import model.Data;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class DbReader implements Reader {

    private final Logger logger;
    SessionFactory sessionFactory;

    public DbReader(SessionFactory sessionFactory) {
        logger = LoggerFactory.getLogger(DbReader.class);
        this.sessionFactory = sessionFactory;
    }

    public List getSymbolByName(String symbolName) {
        Session session = sessionFactory.openSession();
        List symbolList;
        symbolList = session.createQuery("from Symbol where symbol.symbol=:symbol").setParameter("symbol", symbolName).getResultList();
        session.close();
        return symbolList;
    }

    public List<Symbol> getSymbols() {
        List<Symbol> returnedObjects;
        Session session = sessionFactory.openSession();
        returnedObjects = session.createQuery("from Symbol", Symbol.class).getResultList();
        session.close();

        for (Symbol symbol : returnedObjects) {
            var dateTime = readLastDate(symbol);
            symbol.setLastDate(dateTime);
        }
        return returnedObjects;
    }


    public LocalDateTime readLastDate(Symbol symbol) {
        Session session = sessionFactory.openSession();
        List<Symbol> symbolList = getSymbolByName(symbol.getSymbolName());
        symbolList.forEach(s -> System.out.println(s.toString()));

        List resultList = session.createQuery("from BinanceData where dataId.symbol.symbol = :symbol")
                .setParameter("symbol", symbol.getSymbolName())
                .setMaxResults(1)
                .list();

        LocalDateTime result;
        if (resultList.size() != 0) {
            BinanceData bar = (BinanceData) resultList.get(resultList.size() - 1);
            result = bar.getOpenTime();

        } else {
            result = LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0);
        }
        return result;
    }

    @Override
    public List<Data> getData() {
        Session session = sessionFactory.openSession();
        return session.createQuery("from BinanceData", Data.class).getResultList();
    }


}
