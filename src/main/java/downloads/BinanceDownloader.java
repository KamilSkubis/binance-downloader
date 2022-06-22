package downloads;

import com.binance.connector.client.impl.spot.Market;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class BinanceDownloader {

    private Market market;
    private Logger logger;
    private List<String> tickers;

    public BinanceDownloader(Market market) {
        this.market = market;
        logger = LoggerFactory.getLogger(BinanceDownloader.class);
        tickers = new LinkedList<String>();
    }

    public void downloadUSDTcurrenciesKlines() {
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("symbol", "BTCUSDT");
        params.put("interval", "1d");
        params.put("limit", 1);

        String response = market.klines(params);
        JsonArray arr = (JsonArray) JsonParser.parseString(response);
        for (JsonElement e : arr) {
            JsonArray temp = e.getAsJsonArray();
            System.out.println(temp.get(0));
            System.out.println(temp.get(1));
        }

    }

    public List<String> getTickers() {
        logger.info("Initialization: Start downloading ticker names");
        List<String> tickers = new LinkedList<String>();
        String result = market.tickerSymbol(null);
        JsonArray arr = (JsonArray) JsonParser.parseString(result);
        for (JsonElement el : arr) {
            JsonObject json = el.getAsJsonObject();
            String symbol = json.get("symbol").toString();
            String symbolTrimmedFromSpecialCharacters = symbol.substring(1, symbol.length() - 1);
            tickers.add(symbolTrimmedFromSpecialCharacters);
        }
        logger.info("Found: " + tickers.size() + " tickers");
        return tickers;
    }

    public List<Data> downloadKlines(LinkedHashMap<String, Object> params) {
        logger.info("Initialization: Start downloading data for ticker {}", params.get("symbol"));
        List<Data> downloadedData = new LinkedList<>();

        String response = market.klines(params);
        JsonArray arr = (JsonArray) JsonParser.parseString(response);
        for (JsonElement el : arr) {
            Data bar = new Data();
            bar.setTicker(String.valueOf(params.get("symbol")));
            bar.setOpenTime(el.getAsJsonArray().get(0).getAsLong());
            bar.setOpen(el.getAsJsonArray().get(1).getAsDouble());
            bar.setHigh(el.getAsJsonArray().get(2).getAsDouble());
            bar.setLow(el.getAsJsonArray().get(3).getAsDouble());
            bar.setClose(el.getAsJsonArray().get(4).getAsDouble());
            bar.setVolume(el.getAsJsonArray().get(5).getAsDouble());
            downloadedData.add(bar);
        }
        logger.info("data for {} downloaded successfully", params.get("symbol"));
        return downloadedData;
    }

}
