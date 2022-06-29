package persistence;

import model.Binance1d;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;


public class DBWriter {

    public static void writeSymbol(SessionFactory sessionFactory,Symbol symbol) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(symbol);
        transaction.commit();
        session.close();
    }

    public static void writeData(SessionFactory sessionFactory, Binance1d d) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

//
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery cr = cb.createQuery(Symbol.class);
//        Root<Symbol> root = cr.from(Symbol.class);
//        cr.select(root).where(cb.like(root.get("symbol"),d.getSymbol().getSymbol()));

        List<Symbol> symbolList =session.createQuery("from Symbol where symbol.symbol=:symbol")
                .setParameter("symbol",d.getSymbol().getSymbol()).getResultList();
//        List<Symbol> symbolList =session.createQuery("select symbol,id from Symbol where symbol.symbol=:symbol")
//                .setParameter("symbol",d.getSymbol().getSymbol()).getResultList();
        System.out.println("symbolList: " + symbolList.toString());
        if(symbolList.size() == 1){
            Symbol persistentSymbol = session.get(Symbol.class,symbolList.get(0).getId());
            d.setSymbol(persistentSymbol);
        }
        session.save(d);
        transaction.commit();
        session.close();
    }
}
