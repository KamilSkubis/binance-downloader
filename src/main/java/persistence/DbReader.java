package persistence;

import model.BinanceData;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

public class DbReader implements Reader {

    SessionFactory sessionFactory;

    public DbReader(SessionFactory sessionFactory) {
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

        Query query = session.createQuery("from BinanceData where symbol = :symbol");
        query.setParameter("symbol", symbolList.get(0));

        LocalDateTime result;
        if (query.getResultList().size() != 0) {
            BinanceData bar = (BinanceData) query.getResultList().get(query.getResultList().size() - 1);
            result = bar.getOpenTime();

        } else {
            result = LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0);
        }
        return result;
    }


}
