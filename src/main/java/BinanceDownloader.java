import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.connector.client.impl.spot.Market;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

	public Data downloadKlines(LinkedHashMap<String, Object> params) {
		logger.info("Initialization: Start downloading data for ticker {}", params.get("symbol"));
		Data barData = new Data();
		barData.setTicker(String.valueOf(params.get("symbol")));

		String response =  market.klines(params);
		JsonArray arr = (JsonArray) JsonParser.parseString(response);
		for(JsonElement el: arr){
			String s = el.getAsJsonArray().get(0).getAsString();
			barData.pushToOpenTime(s);
			barData.pushOpen(el.getAsJsonArray().get(1).getAsDouble());
			barData.pushHigh(el.getAsJsonArray().get(2).getAsDouble());
			barData.pushLow(el.getAsJsonArray().get(3).getAsDouble());
			barData.pushClose(el.getAsJsonArray().get(4).getAsDouble());
			barData.pushVolume((el.getAsJsonArray().get(5).getAsDouble()));
			barData.pushCloseTime(el.getAsJsonArray().get(6).getAsString());
			barData.pushQuoteAsset(el.getAsJsonArray().get(7).getAsDouble());
			barData.pushNumberTrades(el.getAsJsonArray().get(8).getAsInt());
			barData.pushTakerBuyBase(el.getAsJsonArray().get(9).getAsDouble());
			barData.pushTakerBuyQuote(el.getAsJsonArray().get(10).getAsDouble());
		}
		logger.info("data for {} downloaded successfully", params.get("symbol"));
		return barData;
	}
}
