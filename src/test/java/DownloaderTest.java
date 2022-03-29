
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;
import org.mockito.Mockito;

import com.binance.connector.client.impl.spot.Market;

public class DownloaderTest {

	private String jsonReturn = "[{" + "\"symbol\":" + "\"ticker\"," + "\"price\":" + "\"100\"" + "}]" ;
	
	@Test
	public void getTickers_willReturnTickerList() {
		Market market = Mockito.mock(Market.class);
		BinanceDownloader b = new BinanceDownloader(market);
		Mockito.when(market.tickerSymbol(null)).thenReturn(jsonReturn);
		LinkedList<String> result = new LinkedList<String>();
		result.add("ticker");
		assertEquals(result.get(0),b.getTickers().get(0));
		Mockito.verify(market, Mockito.times(1)).tickerSymbol(null);
	}


}
