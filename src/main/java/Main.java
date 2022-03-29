import java.util.List;

import com.binance.connector.client.impl.SpotClientImpl;

public class Main {
	
	public static void main(String[] args) {
		SpotClientImpl client = new SpotClientImpl();
		BinanceDownloader binance = new BinanceDownloader(client.createMarket());
		
		List<String> result =  binance.getTickers();
		System.out.println(result);
	}
	
}
