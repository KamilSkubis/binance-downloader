import com.binance.connector.client.impl.SpotClientImpl;

public class Main {
	
	public static void main(String[] args) {
		SpotClientImpl client = new SpotClientImpl();
		BinanceDownloader binance = new BinanceDownloader(client.createMarket());
//		binance.downloadUSDTcurrenciesKlines();
		
		String r = client.createMarket().tickerSymbol(null);
		System.out.println(r);
		
		
		
	}
	
}
