package persistence;

import model.Symbol;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final SessionFactory sessionFactory;

    public DataManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Symbol> saveOrUpdateSymbols(List<String> symbolList){
        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> result = dbReader.getSymbolObjListFromDb();

        List<String> onlySymbolNames = new ArrayList<>();
        //convert List<Symbol> to List<String>
        for(Symbol s : result){
            onlySymbolNames.add(s.getSymbolName());
        }

        List<String> diffrences = new ArrayList<>(symbolList);
        diffrences.removeAll(onlySymbolNames);

        if(diffrences.size() > 0){
            System.out.println("Found diffrencess " + diffrences);
            for(String s : diffrences){
                Symbol symbol = new Symbol();
                symbol.setSymbolName(s);
                DBWriter.writeSymbol(sessionFactory,symbol);
            }
            result = dbReader.getSymbolObjListFromDb();
        }
        //TODO: check if downloaded symbolList is smaller than persistent list. Meaning some tickers are not listed anymore.
        return result;
    }

    public List<Symbol> getSymbolList() {
        DbReader dbReader = new DbReader(sessionFactory);
        return dbReader.getSymbolObjListFromDb();
    }

    public List<Symbol> getSymbolsToDownload(List<String> symbolList) {
        List<Symbol> result = new ArrayList<>();
        saveOrUpdateSymbols(symbolList);

        return result;
    }

}
