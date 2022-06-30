package persistence;

import model.Binance1d;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

public class DbReader {

    SessionFactory sessionFactory;

    public DbReader(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public List<Symbol> getSymbolsObjFromDb(String symbolName) {
        Session session = sessionFactory.openSession();
        List<Symbol> symbolList = session.createQuery("from Symbol where symbol.symbol=:symbol")
                .setParameter("symbol", symbolName).getResultList();
        session.close();
        return symbolList;
    }

    public  long readLastDate(Symbol symbol, Long askDate) {
        Session session = sessionFactory.openSession();
        List<Symbol> symbolList = getSymbolsObjFromDb(symbol.getSymbolName());

        Query query = session.createQuery("from Binance1d where binance1d.symbol = :symbol");
        query.setParameter("symbol", symbolList.get(0));
        Binance1d b1 = (Binance1d) query.getResultList().get(0);
        return b1.getOpenTime() - askDate;
    }
}
