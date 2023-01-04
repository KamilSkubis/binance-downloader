import config.Config;
import downloads.BinanceDownloader;
import model.Data;
import model.Symbol;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.DataRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class BinanceRunner {


    private final Logger logger;
    private final String timeframe;
    private final Integer kline_limit;
    private final DataRepository dataRepository;
    private final BinanceDownloader downloader;


    public BinanceRunner(DataRepository dataRepository, BinanceDownloader downloader, Config config) {

        logger = LoggerFactory.getLogger(BinanceRunner.class);
        this.dataRepository = dataRepository;
        this.downloader = downloader;

        timeframe = config.getTimeFrame();
        kline_limit = config.getKlineLimit();
    }

    public void run() {

        List<Symbol> symbols = getPersistentSymbolsWithUSDT();
        HashMap<String, LocalDateTime> symbolNameWithLastDate = getSymbolByDateFromPersistence(symbols);
        List<String> downloadedSymbols = downloadSymbolsFromBinance();

        for (String symbolName : downloadedSymbols) {
            if (!symbolNameWithLastDate.containsKey(symbolName)) {
                Symbol symbol = createSymbolWithDefaultLastDate(symbolName);
//                symbolNameWithLastDate.put(symbol.getSymbolName(), symbol.getLastDate());
                symbols.add(symbol);
                dataRepository.write(symbol);
            }
        }

        final List<LinkedHashMap<String, Object>> params = prepareParams(symbolNameWithLastDate);

        params.parallelStream().forEach(map -> {
            List<Data> data = downloader.downloadKlines(map, symbols);

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
                data.addAll(downloader.downloadKlines(map, symbols));
            }
            data.remove(data.size() - 1);
            dataRepository.write(data);
            data.clear();
        });

    }

    @NotNull
    private Symbol createSymbolWithDefaultLastDate(String symbolName) {
        Symbol symbol = new Symbol();
        symbol.setSymbolName(symbolName);
        return symbol;
    }

    private List<String> downloadSymbolsFromBinance() {
        return downloader.getTickers();
    }

    @NotNull
    private HashMap<String, LocalDateTime> getSymbolByDateFromPersistence(List<Symbol> symbols) {
        HashMap<String, LocalDateTime> symbolNameWithLastDate = new HashMap<>();
        for (Symbol symbol : symbols) {
            LocalDateTime lastDate = dataRepository.readLastDate(symbol);
            symbolNameWithLastDate.put(symbol.getSymbolName(), lastDate);
        }
        return symbolNameWithLastDate;
    }

    private List<Symbol> getPersistentSymbolsWithUSDT() {
        return dataRepository.getSymbolsWithUSDT();
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


}
