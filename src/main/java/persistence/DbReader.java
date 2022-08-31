package persistence;

import model.BinanceData;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DbReader {

    SessionFactory sessionFactory;

    public DbReader(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Symbol> getSymbolObjFromDb(String symbolName) {
        Session session = sessionFactory.openSession();
        List<Symbol> symbolList = session.createQuery("from Symbol where symbol.symbol=:symbol").setParameter("symbol", symbolName).getResultList();
        session.close();
        return symbolList;
    }

    public List<Symbol> getSymbolObjListFromDb() {
        List<Symbol> returnedObjects = new ArrayList<>();
        Session session = sessionFactory.openSession();
        returnedObjects = session.createQuery("from Symbol").getResultList();
        session.close();
        return returnedObjects;
    }


    public LocalDateTime readLastDate(Symbol symbol) {
        Session session = sessionFactory.openSession();
        List<Symbol> symbolList = getSymbolObjFromDb(symbol.getSymbolName());

        Query query = session.createQuery("from BinanceData where symbol = :symbol");
        query.setParameter("symbol", symbolList.get(0));

        LocalDateTime result = null;
        if(query.getResultList().size() != 0) {
             BinanceData bar = (BinanceData) query.getResultList().get(query.getResultList().size() - 1);
             result = bar.getOpenTime();
        }else{
            result = LocalDateTime.of(2010,1,1,0,0,0,0);
        }
        return result;
    }

    public long getLatestIndex() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from BinanceData order by id DESC");
        query.setMaxResults(1);
        BinanceData bar = (BinanceData) query.getSingleResult();

        return  bar.getId();
    }
}
