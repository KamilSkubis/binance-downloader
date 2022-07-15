package persistence;

import model.Binance1d;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
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


    public long readLastDate(Symbol symbol) {
        Session session = sessionFactory.openSession();
        List<Symbol> symbolList = getSymbolObjFromDb(symbol.getSymbolName());

        Query query = session.createQuery("from Binance1d where symbol = :symbol");
        query.setParameter("symbol", symbolList.get(0));
        Binance1d result = (Binance1d) query.getResultList().get(0);
        return result.getOpenTime();
    }
}
