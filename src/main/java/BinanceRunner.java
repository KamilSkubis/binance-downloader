import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import downloads.BinanceDownloader;
import model.Symbol;
import org.hibernate.SessionFactory;
import persistence.DataManager;
import persistence.DbReader;
import persistence.MySQLUtil;

import java.util.List;
import java.util.stream.Collectors;

public class BinanceRunner {

    public void run(){
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market =client.createMarket();
        BinanceDownloader binance = new BinanceDownloader(market);

        List<String> filteredSymbolList =  getListOfSymbolsUSDT(binance,"USDT");
        MySQLUtil mySQLUtil = new MySQLUtil();
        DataManager dataManager = new DataManager(mySQLUtil.getSessionFactory());

        SessionFactory sessionFactory = mySQLUtil.getSessionFactory();

        DbReader dbReader = new  DbReader(sessionFactory);
        List<Symbol> symbolObj =  dbReader.getSymbolObjListFromDb();

    }

    private List<String> getListOfSymbolsUSDT(BinanceDownloader binance, String currency) {
        List<String> symbolList = binance.getTickers();
        List<String> result = symbolList.stream().filter(s -> s.endsWith(currency)).collect(Collectors.toList());
        return result;
    }

}
