package persistence;

import model.Symbol;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final SessionFactory sessionFactory;
    private final Logger logger;

    public DataManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        logger = LoggerFactory.getLogger(DataManager.class);
    }

    public List<Symbol> saveOrUpdateSymbols(List<String> symbolList){
        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> result = dbReader.getSymbolObjListFromDb();

        List<String> onlySymbolNames = new ArrayList<>();
        //convert List<Symbol> to List<String>
        for(Symbol s : result){
            onlySymbolNames.add(s.getSymbolName());
        }

        List<String> differences = new ArrayList<>(symbolList);
        differences.removeAll(onlySymbolNames);


        if(differences.size() > 0){
            logger.info("Found differences in Symbols table: " + differences);
            Writer writer = new DbWriter(sessionFactory); //TODO tight coupling -- refactor this class
            for(String s : differences){
                Symbol symbol = new Symbol();
                symbol.setSymbolName(s);
                writer.write(symbol);
            }
            result = dbReader.getSymbolObjListFromDb();
        }
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
