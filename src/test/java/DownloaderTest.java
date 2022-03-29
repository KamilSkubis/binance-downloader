
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import com.binance.connector.client.impl.spot.Market;

public class DownloaderTest {

	private String jsonReturn = "[{" + "\"symbol\":" + "\"ticker\"," + "\"price\":" + "\"100\"" + "}]";
	private String jsonArray = "[[1499040000000,\"0.01634790\",\"0.80000000\",\"0.01575800\",\"0.01577100\",\"148976.11427815\",1499644799999,\"2434.19055334\",308,\"1756.87402397\",\"28.46694368\",\"17928899.62484339\" ]]";



	@Test
	public void getTickers_willReturnTickerList() {
		Market market = Mockito.mock(Market.class);
		BinanceDownloader b = new BinanceDownloader(market);
		Mockito.when(market.tickerSymbol(null)).thenReturn(jsonReturn);
		LinkedList<String> result = new LinkedList<String>();
		result.add("ticker");
		assertEquals(result.get(0), b.getTickers().get(0));
		Mockito.verify(market, Mockito.times(1)).tickerSymbol(null);
	}

	@Test
	public void downloadKlines_ReturnBar() {
		Market market = Mockito.mock(Market.class);

		BinanceDownloader b = new BinanceDownloader(market);
		LinkedHashMap<String, Object> params = setUpParams();
		Mockito.when(market.klines(params)).thenReturn(jsonArray);

		assertEquals("test", b.downloadKlines(params).getTicker());
	}


	@Test
	public void downloadKlines_ReturnCorrectDataFromJsonArray(){
		Market market = Mockito.mock(Market.class);
		Mockito.when(market.klines(setUpParams())).thenReturn(jsonArray);
		BinanceDownloader b = new BinanceDownloader(market);
		LinkedHashMap<String,Object> params = setUpParams();
		Data data = b.downloadKlines(params);
		assertEquals("1499040000000",data.getOpenTime());
	}

	@NotNull
	private LinkedHashMap<String, Object> setUpParams() {
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("symbol", "test");
		params.put("interval", "1d");
		params.put("limit", 1);
		return params;
	}

}
