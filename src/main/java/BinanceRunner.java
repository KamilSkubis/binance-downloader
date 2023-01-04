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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

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
//        List<String> symbolsUSDT = getSingleCoin(binance);

        logger.info("USDT symbols already in database: " + symbolsUSDT.size());

        DbReader dbReader = new DbReader(sessionFactory);
        List<Symbol> symbols = dbReader.getSymbolObjListFromDb();
        HashMap<String, LocalDateTime> latestDateTimePerSymbol = new HashMap<>();

        for (Symbol symbol : symbols) {
            LocalDateTime lastDate = dbReader.readLastDate(symbol);
            latestDateTimePerSymbol.put(symbol.getSymbolName(), lastDate);
        }

        for (String ticker : symbolsUSDT) {
            if (!latestDateTimePerSymbol.containsKey(ticker)) {
                Symbol symbol = new Symbol();
                symbol.setSymbolName(ticker);
                symbols.add(symbol);
                LocalDateTime startTime = LocalDateTime.of(2010, 1, 1, 0, 0, 0);
                latestDateTimePerSymbol.put(ticker, startTime);
            }
        }

        logger.info("symbol Time from database: ");
        logger.info(latestDateTimePerSymbol.toString());

        final List<LinkedHashMap<String, Object>> params = prepareParams(latestDateTimePerSymbol);

        params.parallelStream().forEach(map -> {
            List<Data> data = binance.downloadKlines(map,symbols);
            Writer writer = new DbWriter(sessionFactory);

            while (data.size() % kline_limit == 0) {

                LocalDateTime nextDate = data.get(data.size() - 1).getOpenTime().plusMinutes(1);

                var calculatedDate = nextDate.toLocalDate();
                if (calculatedDate.equals(LocalDate.now())) {
                    logger.info("calculated date is equal to today date. Breaking while loop");
                    break;
                }

                Instant instant = nextDate.toInstant(UTC);
                Long date = instant.toEpochMilli();

                map.replace("startTime", date);
                data.addAll(binance.downloadKlines(map, symbols));
            }
            data.remove(data.size() - 1);
            writer.write(data);
            data.clear();
        });

    }


    private List<LinkedHashMap<String, Object>> prepareParams(HashMap<String, LocalDateTime> symbolTimeFromDb) {
        List<LinkedHashMap<String, Object>> preparedParamList = new ArrayList<>();

        symbolTimeFromDb.keySet().stream().forEach(key -> {
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put("symbol", key);
            params.put("interval", timeframe);
            params.put("limit", kline_limit);
            params.put("startTime", String.valueOf(symbolTimeFromDb.get(key).toInstant(UTC).toEpochMilli()));
            preparedParamList.add(params);
        });

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

    private List<String> getSingleCoin(BinanceDownloader binance) {
        List<String> symbolList = binance.getTickers();
        return symbolList.stream().filter(s -> s.endsWith("BTCUSDT")).collect(Collectors.toList());
    }

}
