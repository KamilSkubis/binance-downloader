import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	
	public static void main(String[] args) {
		
		Binance binance = new Binance();
//		binance.downloadUSDTcurrenciesKlines();
		
		binance.getTickers();
		
		
	}
	
}
