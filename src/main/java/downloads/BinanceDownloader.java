package downloads;

import com.binance.connector.client.impl.spot.Market;
import com.google.gson.*;
import downloads.deserializeJSON.BinanceSymbolInner;
import downloads.deserializeJSON.BinanceSymbolOuter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class BinanceDownloader {

    private final Market market;
    private final Logger logger;
    private final List<String> tickers;

    private int usedWeight;

    public BinanceDownloader(Market market) {
        this.market = market;
        logger = LoggerFactory.getLogger(BinanceDownloader.class);
        tickers = new LinkedList<String>();
        usedWeight = 0;
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

    public List<String> getSymbols(){
        String result = market.tickerSymbol(null);
        System.out.println(result);
        return null;
    }

    public List<String> getTickers() {
        logger.info("Initialization: Start downloading ticker names");
        List<String> tickers = new LinkedList<String>();
        String result = market.tickerSymbol(null);
        System.out.println(result);

        Gson gson = new Gson();
        BinanceSymbolOuter deserializedObject = gson.fromJson(result, BinanceSymbolOuter.class);

        BinanceSymbolInner[] inner = gson.fromJson(deserializedObject.symbolList, BinanceSymbolInner[].class);

        for(int i=0; i<inner.length; i++){
            tickers.add(inner[i].getSymbol());
        }

        return tickers;
   }

    public List<Data> downloadKlines(LinkedHashMap<String, Object> params) {
        logger.info("Initialization: Start downloading data for ticker {}", params.get("symbol"));
        List<Data> downloadedData = new LinkedList<>();

        String symbol = (String.valueOf(params.get("symbol")));

        String response = market.klines(params);
        JsonArray arr = (JsonArray) JsonParser.parseString(response);
        for (JsonElement el : arr) {
            Data bar = new Data();
            bar.setSymbol(symbol);
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
