import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.connector.client.impl.SpotClientImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Binance {

	private SpotClientImpl client;
	private Logger logger;
	
	
	public Binance() {
		this.client = new SpotClientImpl();
		logger = LoggerFactory.getLogger(Binance.class);
	}

	public void downloadUSDTcurrenciesKlines() {
		LinkedHashMap<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("symbol", "BTCUSDT");
		params.put("interval", "1d");
		params.put("limit", 1);
		
		String response = client.createMarket().klines(params);
		JsonArray arr = (JsonArray) JsonParser.parseString(response);
		
		for(JsonElement e : arr) {
			JsonArray temp = e.getAsJsonArray();
			System.out.println(temp.get(0));
			System.out.println(temp.get(1));
		}
		
	}

	public List<String> getTickers() {
		List<String> tickers = new LinkedList<String>();
		LinkedHashMap<String,Object> params = new LinkedHashMap<String,Object>();
		String response =  client.createMarket().tickerSymbol(params);

		
		JsonArray arr = (JsonArray) JsonParser.parseString(response);
		for(JsonElement el : arr) {
			JsonObject json = el.getAsJsonObject();
			String symbol =  json.get("symbol").toString();
			tickers.add(symbol);
		}
		
		logger.info("Found: " + tickers.size() + " tickers" );
		return tickers;
	}
	
	
}
