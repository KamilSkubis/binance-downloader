import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import downloads.BinanceDownloader;
import model.Data;
import model.Symbol;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import persistence.DBWriter;
import persistence.DbReader;
import persistence.MySQLUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class BinanceRunner {

    final private BinanceDownloader binance;
    final private SessionFactory sessionFactory;

    final private MySQLUtil mySQLUtil;

    public BinanceRunner() {
        binance = configureDownloader();
        mySQLUtil = new MySQLUtil();
        sessionFactory = mySQLUtil.getSessionFactory();
    }

    public void run() {

        //sciagnij dane odnośnie tickerów, filtruj do USDT
//        List<String> filteredSymbolList = getListOfSymbolsUSDT(binance, "USDT");
        List<String> filteredSymbolList = List.of("BTCUSDT");
        System.out.println("downloaded tickers: " + filteredSymbolList.size());

        //pobierz dane odnośnie symbolów z bazy danych i pobierz ostatni czas z bazy danych
        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> symbolObj = dbReader.getSymbolObjListFromDb();
        HashMap<String, LocalDateTime> symbolTimeFromDb = new HashMap<>();

        Iterator symbolObjIterator = symbolObj.listIterator();

        for (Symbol symbol : symbolObj) {
            LocalDateTime lastDate = dbReader.readLastDate(symbol);
            symbolTimeFromDb.put(symbol.getSymbolName(), lastDate);
        }


        System.out.println("symbol Time from database: ");
        System.out.println(symbolTimeFromDb);

        //utwórz paramList dla każdego symbolu uwzględniając czas ostatniej świecy z db
        //przechodzę po filteredList pobranych z binance
        //do każdego symbolu dodaję

        final List<LinkedHashMap<String, Object>> params = prepareParams(filteredSymbolList, symbolTimeFromDb);

        //sciagnij dane z binance
        for (LinkedHashMap<String, Object> map : params) {
            List<Data> data = binance.downloadKlines(map); //inside hard coded binance1d
            data.forEach(d -> DBWriter.writeData(sessionFactory, d));

            int dataSize = data.size();

            while (dataSize == 1000) {
                Symbol symbol = new Symbol();
                symbol.setSymbolName(String.valueOf(map.get("symbol")));
                final LocalDateTime lastDate = dbReader.readLastDate(symbol);
                final HashMap<String,LocalDateTime> updateDateHashMap = new HashMap<>();
                updateDateHashMap.put(symbol.getSymbolName(),lastDate);

                final List<LinkedHashMap<String, Object>> moreParams = prepareParams(filteredSymbolList, updateDateHashMap);

                for(LinkedHashMap<String,Object> moreMap : moreParams) {
                    final List<Data> moreData = binance.downloadKlines(moreMap); //inside hard coded binance1d
                    moreData.forEach(d -> DBWriter.writeData(sessionFactory, d));
                    dataSize = moreData.size();
                }

            }

        }
    }

    private List<LinkedHashMap<String, Object>> prepareParams(List<String> symbolsFromBinance, HashMap<String, LocalDateTime> symbolTimeFromDb) {
        List<LinkedHashMap<String, Object>> preparedParamList = new ArrayList<>();
        for (String symbol : symbolsFromBinance) {
            System.out.println("sprawdzam czy symbol " + symbol + " jest w bazie " + symbolTimeFromDb.containsKey(symbol));
            if (!symbolTimeFromDb.containsKey(symbol)) {
                LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                params.put("symbol", symbol);
                params.put("interval", "1m"); // TODO for daily 1d
                params.put("limit", 3); // TODO max 1000 , default 500
                preparedParamList.add(params);
            } else {
                System.out.println("symbol " + symbol + " , znajduje się w bazie danych");

                LocalDateTime dateInDb = symbolTimeFromDb.get(symbol);
                LocalDateTime newStartDate = dateInDb.plusMinutes(1); // TODO change this when downloading daily

                System.out.println("znaleziono datę: " + dateInDb + " nowa data: " + newStartDate);
                Instant inst = newStartDate.toInstant(ZoneOffset.UTC);
                long convertedTime = inst.toEpochMilli();

                LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                params.put("symbol", symbol);
                params.put("interval", "1m"); // TODO for daily 1d
                params.put("startTime", convertedTime);
                params.put("limit", 1000); //  max 1000 , default 500
                preparedParamList.add(params);
            }
        }
        return preparedParamList;
    }

    @NotNull
    private BinanceDownloader configureDownloader() {
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market = client.createMarket();
        BinanceDownloader binance = new BinanceDownloader(market);
        return binance;
    }

    private List<String> getListOfSymbolsUSDT(BinanceDownloader binance, String currency) {
        List<String> symbolList = binance.getTickers();
        List<String> result = symbolList.stream().filter(s -> s.endsWith(currency)).collect(Collectors.toList());
        return result;
    }

}
