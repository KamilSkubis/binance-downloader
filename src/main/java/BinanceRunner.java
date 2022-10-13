import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import config.Config;
import config.ConfigLocation;
import config.ConfigReader;
import downloads.BinanceDownloader;
import model.Data;
import model.Symbol;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.BatchWriterMultiThreaded;
import persistence.DBWriter;
import persistence.DbReader;
import persistence.MySQLUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BinanceRunner {

    final private BinanceDownloader binance;
    private final SessionFactory sessionFactory;
    private final Logger logger;
    private final String timeframe;
    private final Integer kline_limit;


    public BinanceRunner() {
        binance = configureDownloader();
        sessionFactory = MySQLUtil.getSessionFactory();
        logger = LoggerFactory.getLogger(BinanceRunner.class);

        ConfigLocation configLocation = new ConfigLocation();
        ConfigReader configReader = new ConfigReader();
        Config config = configReader.read(configLocation);

        timeframe = config.getTimeFrame();
        kline_limit = config.getKlineLimit();

        //this is for testing only;
//        timeframe = "1m";
//        kline_limit= "50";

    }

    public void run() {
        Long startTime = System.currentTimeMillis();
        List<String> filteredSymbolList = getListOfSymbolsUSDT(binance, "USDT");
        logger.info("downloaded tickers: " + filteredSymbolList.size());

        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> symbolObj = dbReader.getSymbolObjListFromDb();
        HashMap<String, LocalDateTime> symbolTimeFromDb = new HashMap<>();

        for (Symbol symbol : symbolObj) {
            LocalDateTime lastDate = dbReader.readLastDate(symbol);
            symbolTimeFromDb.put(symbol.getSymbolName(), lastDate);
        }

        logger.info("symbol Time from database: ");
        logger.info(symbolTimeFromDb.toString());

        final List<LinkedHashMap<String, Object>> params = prepareParams(filteredSymbolList, symbolTimeFromDb);

        for (LinkedHashMap<String, Object> map : params) {

            List<Data> data = binance.downloadKlines(map);
            DBWriter.writeDatainBatch(sessionFactory,data);

            int dataSize = data.size();

            while (dataSize == kline_limit) {

                LocalDateTime nextDate = data.get(data.size() - 1).getOpenTime().plusMinutes(1);
                Instant instant = nextDate.toInstant(ZoneOffset.UTC);
                Long date = instant.toEpochMilli();

                map.replace("startTime", date);
                List<Data> downloadedData = binance.downloadKlines(map);
                DBWriter.writeDatainBatch(sessionFactory,downloadedData);
                dataSize = downloadedData.size();
            }

        }

        Long endTime = System.currentTimeMillis();
        Long duration = endTime - startTime;
        logger.info("Program took " + duration + "ms or " + duration/1000 + "s");
    }


    private List<LinkedHashMap<String, Object>> prepareParams(List<String> symbolsFromBinance, HashMap<String, LocalDateTime> symbolTimeFromDb) {
        List<LinkedHashMap<String, Object>> preparedParamList = new ArrayList<>();
        for (String symbol : symbolsFromBinance) {

            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put("symbol", symbol);
            params.put("interval", timeframe); //  for daily timeframe use 1d
            params.put("limit", Integer.valueOf(kline_limit));    //default 500 max 1000

            System.out.println("check if " + symbol + " is in database " + symbolTimeFromDb.containsKey(symbol));

            if (!symbolTimeFromDb.containsKey(symbol)) {
                LocalDateTime newStartDate = LocalDateTime.of(2010, 1, 1, 0, 0, 0);
                Instant instant = newStartDate.toInstant(ZoneOffset.UTC);
                long convertedTime = instant.toEpochMilli();
                params.put("startTime", convertedTime);

            } else {
                System.out.println("symbol " + symbol + " , found in database");

                final LocalDateTime dateInDb = symbolTimeFromDb.get(symbol);
                final LocalDateTime newStartDate = dateInDb.plusMinutes(1); // TODO change this when downloading daily, or refactor

                System.out.println("found latest date: " + dateInDb + " new calculated date: " + newStartDate);
                Instant inst = newStartDate.toInstant(ZoneOffset.UTC);
                long convertedTime = inst.toEpochMilli();

                params.put("startTime", convertedTime);
            }
            preparedParamList.add(params);
        }
        return preparedParamList;
    }

    @NotNull
    private BinanceDownloader configureDownloader() {
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market = client.createMarket();
        return new BinanceDownloader(market);
    }

    private List<String> getListOfSymbolsUSDT(BinanceDownloader binance, String currency) {
        List<String> symbolList = binance.getTickers();
        return symbolList.stream().filter(s -> s.endsWith(currency)).collect(Collectors.toList());
    }

}
