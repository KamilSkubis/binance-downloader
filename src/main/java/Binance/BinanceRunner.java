package Binance;

import config.Config;
import downloads.Downloader;
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
import java.util.LinkedHashMap;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class BinanceRunner {


    private final Logger logger;
    private final String timeframe;
    private final Integer kline_limit;
    private final DataRepository dataRepository;
    private final Downloader downloader;


    public BinanceRunner(DataRepository dataRepository, Downloader downloader, Config fileConfig) {

        logger = LoggerFactory.getLogger(BinanceRunner.class);
        this.dataRepository = dataRepository;
        this.downloader = downloader;

        timeframe = fileConfig.getTimeFrame();
        kline_limit = fileConfig.getKlineLimit();
    }

    public void run() {
        LocalDateTime savedTime = LocalDateTime.now();


        List<String> downloadedSymbols = downloadSymbolsFromBinance();
        logger.debug("start synchronization with db");
        dataRepository.sychronizeDownloadedSymbolsWithDb(downloadedSymbols);
        logger.debug("symbols are synchronized");

        logger.debug("getting list of symbols");
        List<Symbol> symbols = dataRepository.getSymbols();
        logger.info("selected symbols: {}", symbols.size());

        final List<LinkedHashMap<String, Object>> params = prepareParams(symbols);

        params.stream().forEach(map -> {
            List<Data> data = downloader.downloadKlines(map, symbols);
            if (data.size() == 0) {
                return;
            }
            logger.info("downloaded data for ticker {} size: {}", data.get(0).getSymbol().getSymbolName(), data.size());

            symbols.stream()
                    .filter(s -> s.getSymbolName() == map.get("symbol"))
                    .limit(1)
                    .forEach(s -> s.setLastDate(data.get(data.size() - 1).getOpenTime()));


            while (data.size() % kline_limit == 0) {
                var symbol = symbols.stream()
                        .filter(s -> s.getSymbolName() == map.get("symbol"))
                        .findAny()
                        .orElse(null);

                LocalDateTime modifiedDate = addOneToDateTime(symbol);


                var calculatedDate = modifiedDate.toLocalDate();
                if (calculatedDate.equals(LocalDate.now())) {
                    logger.debug("calculated date is equal to today date. Breaking while loop");
                    break;
                }

                Instant instant = modifiedDate.toInstant(UTC);
                Long date = instant.toEpochMilli();

                map.replace("startTime", date);
                logger.debug("modified params: " + map);

                List<Data> moreDownloadedData = downloader.downloadKlines(map, symbols);
                if (moreDownloadedData.size() != 0) {
                    data.addAll(moreDownloadedData);
                }
            }

            var lastOpenTime = data.get(data.size() - 1).getDataId().getOpenTime();
            switch (timeframe) {
                case "1d":
                    var lastDateIsEqualToDownloadedDate = lastOpenTime.toLocalDate().isEqual(savedTime.toLocalDate());
                    logger.debug("checking condition to remove last data from list: {}", lastDateIsEqualToDownloadedDate);
                    if (lastDateIsEqualToDownloadedDate) {
                        data.remove(data.get(data.size() - 1));
                    }
                    break;
                case "1m":
                    var lastHourAndMinutesIsEqualToDownloadedTime = lastOpenTime.getHour() == savedTime.getHour() && lastOpenTime.getMinute() == savedTime.getMinute();
                    logger.debug("checking condition to remove last data from list: {}", lastHourAndMinutesIsEqualToDownloadedTime);
                    if (lastHourAndMinutesIsEqualToDownloadedTime) {
                        data.remove(data.get(data.size() - 1));
                    }
                    break;
                default:
                    new RuntimeException();
            }


            dataRepository.write(data);
            data.clear();
        });

    }

    private List<String> downloadSymbolsFromBinance() {
        return downloader.getTickers();
    }


    private List<LinkedHashMap<String, Object>> prepareParams(List<Symbol> symbols) {
        List<LinkedHashMap<String, Object>> preparedParamList = new ArrayList<>();

        for (Symbol symbol : symbols) {

            LocalDateTime modifiedDate = addOneToDateTime(symbol);
            logger.debug("mod params: timeframe " + timeframe);
            logger.debug(symbol.getLastDate() + " modified: " + modifiedDate);

            var params = new LinkedHashMap<String, Object>();
            params.put("symbol", symbol.getSymbolName());
            params.put("interval", timeframe);
            params.put("limit", kline_limit);
            params.put("startTime", String.valueOf(modifiedDate.toInstant(UTC).toEpochMilli()));
            preparedParamList.add(params);
            logger.debug("prepared params: " + params);
        }

        return preparedParamList;
    }

    @NotNull
    private LocalDateTime addOneToDateTime(Symbol symbol) {
        LocalDateTime modifiedDate;
        switch (timeframe) {
            case "1d":
                modifiedDate = symbol.getLastDate().plusDays(1);
                logger.debug("mod params: adding one day");
                break;
            case "1m":
                modifiedDate = symbol.getLastDate().plusMinutes(1);
                logger.debug("mod params: adding one minute");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + timeframe);
        }
        return modifiedDate;
    }


}
