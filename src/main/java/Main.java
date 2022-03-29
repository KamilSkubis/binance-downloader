import java.util.LinkedHashMap;
import java.util.List;

import com.binance.connector.client.impl.SpotClientImpl;

public class Main {
	
	public static void main(String[] args) {
		SpotClientImpl client = new SpotClientImpl();
		BinanceDownloader binance = new BinanceDownloader(client.createMarket());

		LinkedHashMap<String,Object> params = new LinkedHashMap<>();
		params.put("symbol", "BTCUSDT");
		binance.downloadKlines(params);


	}
	
}
