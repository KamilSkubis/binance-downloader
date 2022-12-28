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
import persistence.DbReader;
import persistence.DbWriter;
import persistence.MySQLUtil;
import persistence.Writer;

import java.time.Instant;
import java.time.LocalDate;
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
        binance = configureBinanceDownloader();
        sessionFactory = MySQLUtil.getSessionFactory();
        logger = LoggerFactory.getLogger(BinanceRunner.class);

        ConfigLocation configLocation = new ConfigLocation();
        ConfigReader configReader = new ConfigReader();
        Config config = configReader.read(configLocation);

        timeframe = config.getTimeFrame();
        kline_limit = config.getKlineLimit();


    }

    public void run() {

        List<String> symbolsUSDT = getListOfSymbolsUSDT(binance);
        logger.info("USDT symbols already in database: " + symbolsUSDT.size());

        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> symbolObj = dbReader.getSymbolObjListFromDb();
        HashMap<String, LocalDateTime> latestDateTimePerSymbol = new HashMap<>();

        for (Symbol symbol : symbolObj) {
            LocalDateTime lastDate = dbReader.readLastDate(symbol);
            latestDateTimePerSymbol.put(symbol.getSymbolName(), lastDate);
        }

        logger.info("symbol Time from database: ");
        logger.info(latestDateTimePerSymbol.toString());

        final List<LinkedHashMap<String, Object>> params = prepareParams(symbolsUSDT, latestDateTimePerSymbol);

        params.parallelStream().forEach(map -> {
            List<Data> data = binance.downloadKlines(map);
            Writer writer = new DbWriter(sessionFactory);

            while (data.size() % kline_limit == 0) {

                LocalDateTime nextDate = data.get(data.size() - 1).getOpenTime().plusMinutes(1);

                var calculatedDate = nextDate.toLocalDate();
                if (calculatedDate.equals(LocalDate.now())) {
                    logger.info("calculated date is equal to today date. Breaking while loop");
                    break;
                }

                Instant instant = nextDate.toInstant(ZoneOffset.UTC);
                Long date = instant.toEpochMilli();

                map.replace("startTime", date);
                data.addAll(binance.downloadKlines(map));
            }

            data.remove(data.size() - 1);
            writer.write(data);
            data.clear();
        });

    }


    private List<LinkedHashMap<String, Object>> prepareParams(List<String> symbolsFromBinance, HashMap<String, LocalDateTime> symbolTimeFromDb) {
        List<LinkedHashMap<String, Object>> preparedParamList = new ArrayList<>();
        for (String symbol : symbolsFromBinance) {

            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put("symbol", symbol);
            params.put("interval", timeframe);
            params.put("limit", kline_limit);

            System.out.println("check if " + symbol + " is in database " + symbolTimeFromDb.containsKey(symbol));

            if (!symbolTimeFromDb.containsKey(symbol)) {
                LocalDateTime newStartDate = LocalDateTime.of(2010, 1, 1, 0, 0, 0);
                Instant instant = newStartDate.toInstant(ZoneOffset.UTC);
                long convertedTime = instant.toEpochMilli();
                params.put("startTime", convertedTime);

            } else {
                System.out.println("symbol " + symbol + " , found in database");

                final LocalDateTime dateInDb = symbolTimeFromDb.get(symbol);
                final LocalDateTime newStartDate = dateInDb.plusMinutes(1); // this should work for all timeframes

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
    private BinanceDownloader configureBinanceDownloader() {
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market = client.createMarket();
        return new BinanceDownloader(market);
    }

    private List<String> getListOfSymbolsUSDT(BinanceDownloader binance) {
        List<String> symbolList = binance.getTickers();
        return symbolList.stream().filter(s -> s.endsWith("USDT")).collect(Collectors.toList());
    }

}
