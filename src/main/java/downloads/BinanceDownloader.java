package downloads;

import com.binance.connector.client.impl.spot.Market;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import downloads.deserializeJSON.BinanceKlinesOuter;
import downloads.deserializeJSON.BinanceSymbolInner;
import downloads.deserializeJSON.BinanceSymbolOuter;
import model.BinanceData;
import model.Data;
import model.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BinanceDownloader implements Downloader {

    private final Market market;
    private final Logger logger;

    private int usedWeight;
    private int usedWeight1m;

    public BinanceDownloader(Market market) {
        logger = LoggerFactory.getLogger(BinanceDownloader.class);
        this.market = market;
        usedWeight = 0;
        usedWeight1m = 0;
    }

    @Override
    public List<String> getTickers() {
        logger.info("Initialization: Start downloading ticker names");
        List<String> tickers = new LinkedList<>();
        String result = market.tickerSymbol(null);

        Gson gson = new Gson();
        BinanceSymbolOuter deserializedObject = gson.fromJson(result, BinanceSymbolOuter.class);

        BinanceSymbolInner[] inner = gson.fromJson(deserializedObject.symbolList, BinanceSymbolInner[].class);

        for (BinanceSymbolInner binanceSymbolInner : inner) {
            tickers.add(binanceSymbolInner.getSymbol());
        }

        updateUsedWeightAfterCall(deserializedObject.usedWeight);
        return tickers;
    }

    private void updateUsedWeightAfterCall(int deserializedObject) {
        usedWeight = deserializedObject;
        usedWeight1m = deserializedObject;
    }

    @Override
    public List<Data> downloadKlines(LinkedHashMap<String, Object> params, List<Symbol> symbols) {
        logger.info("Initialization: Start downloading data for ticker {}", params.get("symbol"));
        List<Data> downloadedData = new LinkedList<>();

        List<Symbol> filteredSymbol = symbols.stream()
                .filter(symbol -> symbol.getSymbolName() == params.get("symbol"))
                .collect(Collectors.toList());
        Symbol symbol = filteredSymbol.get(0);


        String response = market.klines(params);

        Gson gson = new Gson();
        BinanceKlinesOuter binanceKlinesOuter = gson.fromJson(response, BinanceKlinesOuter.class);

        JsonArray arr = (JsonArray) JsonParser.parseString(binanceKlinesOuter.data);
        for (JsonElement el : arr) {
            Data bar = new BinanceData();
            bar.setSymbol(symbol);
            bar.setOpenTime(convertToLocalDateTime(el.getAsJsonArray().get(0).getAsLong()));
            bar.setOpen(el.getAsJsonArray().get(1).getAsDouble());
            bar.setHigh(el.getAsJsonArray().get(2).getAsDouble());
            bar.setLow(el.getAsJsonArray().get(3).getAsDouble());
            bar.setClose(el.getAsJsonArray().get(4).getAsDouble());
            bar.setVolume(el.getAsJsonArray().get(5).getAsDouble());
            downloadedData.add(bar);
        }

        logger.info("data for {} downloaded successfully, downloaded {} bars", params.get("symbol"), downloadedData.size());

        usedWeight = binanceKlinesOuter.usedWeight;
        usedWeight1m = binanceKlinesOuter.usedWeight1m;
        usedWeightThreadSleep();

        return downloadedData;
    }

    private void usedWeightThreadSleep() {
        logger.info("used weight: " + usedWeight + " used weight 1m: " + usedWeight1m);
        if (usedWeight > 300 || usedWeight1m > 300) {
            try {
                int sleepTime = 30000;
                logger.info("used weight exceed limit, sleeping for {} milliseconds", sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private LocalDateTime convertToLocalDateTime(long intTime) {
        return Instant.ofEpochMilli(intTime).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

}
