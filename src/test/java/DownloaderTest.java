
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.List;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import com.binance.connector.client.impl.spot.Market;

public class DownloaderTest {

	private String jsonReturn = "[{" + "\"symbol\":" + "\"ticker\"," + "\"price\":" + "\"100\"" + "}]" ;
	
	@Test
	public void giveMeSomething() {
		Market market = Mockito.mock(Market.class);
		BinanceDownloader b = new BinanceDownloader(market);
		Mockito.when(market.tickerSymbol(null)).thenReturn(jsonReturn);
		LinkedList<String> result = new LinkedList<String>();
		result.add("ticker");
		assertEquals(result.get(0),b.getMeTickers().get(0));
		Mockito.verify(market, Mockito.times(1)).tickerSymbol(null);
	}
	
	
	
	
//	@Test
//	public void prepareListOfTickers() {
//		BinanceDownloader b = Mockito.spy(new BinanceDownloader());
//
//		Market client = Mockito.mock(Market.class);
//		LinkedHashMap<String,Object> p = Mockito.mock(LinkedHashMap.class);
//		
//		Mockito.when(client.tickerSymbol(p)).thenReturn("1");
//		
//		b.getTickers();
//		Mockito.verify(client, Mockito.times(1)).tickerSymbol(p);
//	}
//
//	
//	@Test()
//	public void downloadKlines_will_call() {
//		BinanceDownloader b = new BinanceDownloader();
//		b.getKlines();
//		
//	}

}
