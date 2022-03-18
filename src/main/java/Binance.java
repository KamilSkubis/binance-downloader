import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.utils.JSONParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Binance {

	private SpotClientImpl client;
	
	public Binance() {
		this.client = new SpotClientImpl();
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
	
	
}
