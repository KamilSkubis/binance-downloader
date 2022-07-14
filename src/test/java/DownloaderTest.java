import com.binance.connector.client.impl.spot.Market;
import downloads.Data;
import model.Binance1d;
import downloads.BinanceDownloader;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DownloaderTest {

//	[
//			[
//			1499040000000,      // Open time
//			"0.01634790",       // Open
//			"0.80000000",       // High
//			"0.01575800",       // Low
//			"0.01577100",       // Close
//			"148976.11427815",  // Volume
//			1499644799999,      // Close time
//			"2434.19055334",    // Quote asset volume
//			308,                // Number of trades
//			"1756.87402397",    // Taker buy base asset volume
//			"28.46694368",      // Taker buy quote asset volume
//			"17928899.62484339" // Ignore.
//			]
//			]

    private final String jsonReturnWithoutSelectedWeightUsed = "[{" + "\"symbol\":" + "\"ticker\"," + "\"price\":" + "\"100\"" + "}]";
    private final String jsonReturn = "{\"data\":[{\"symbol\":\"BTC\",\"price\":\"100\"}],\"x-mbx-used-weight\":\"10\"" +
            ",\"x-mbx-used-weight-1m\":\"2\"}";
    private final String jsonArray = "[[1499040000000,\"0.01634790\",\"0.80000000\",\"0.01575800\",\"0.01577100\",\"148976.11427815\",1499644799999,\"2434.19055334\",308,\"1756.87402397\",\"28.46694368\",\"17928899.62484339\" ]]";
    private List<Data> downloadedData;

    @Before
    public void setUp() {
        getDataFromJsonTestArray();
    }


    @Test
    public void getTickers_willReturnTickerList() {
        Market market = Mockito.mock(Market.class);
        BinanceDownloader b = new BinanceDownloader(market);
        Mockito.when(market.tickerSymbol(null)).thenReturn(jsonReturn);
        LinkedList<String> result = new LinkedList<String>();
        result.add("BTC");
        assertEquals(result.get(0), b.getTickers().get(0));
        Mockito.verify(market, Mockito.times(1)).tickerSymbol(null);
    }

    @Test
    public void downloadKlines_ReturnTicker() {
        Market market = Mockito.mock(Market.class);
        BinanceDownloader b = new BinanceDownloader(market);
        LinkedHashMap<String, Object> params = setUpParams();
        Mockito.when(market.klines(params)).thenReturn(jsonArray);

        assertEquals("test", b.downloadKlines(params).get(0).getSymbol());
    }


    @Test
    public void downloadKlines_ReturnCorrectDataFromJsonArray() {
        assertEquals(1499040000000L,
                downloadedData.get(0).getOpenTime(), 1);
    }

    @Test
    public void downloadKlines_ReturnCorrectOpenPrice() {
        assertEquals(0.01634790,
                downloadedData.get(0).getOpen(), 0.00001);
    }


    @Test
    public void downloadKlines_ReturnCorrectHighPrice() {
        assertEquals(0.80000000,
                downloadedData.get(0).getHigh(), 0.00001);
    }

    @Test
    public void downloadKlines_ReturnCorrectLowPrice() {
        assertEquals(0.01575800,
                downloadedData.get(0).getLow(), 0.00001);
    }


    @Test
    public void downloadKlines_ReturnCorrectClosePrice() {
        assertEquals(0.01577100,
                downloadedData.get(0).getClose(), 0.00001);
    }

    @Test
    public void downloadKlines_ReturnCorrectVolume() {
        assertEquals(148976.11427815,
                downloadedData.get(0).getVolume(), 0.00001);
    }


    @NotNull
    private LinkedHashMap<String, Object> setUpParams() {
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("symbol", "test");
        params.put("interval", "1d");
        params.put("limit", 1);
        return params;
    }

    private void getDataFromJsonTestArray() {
        Market market = Mockito.mock(Market.class);
        Mockito.when(market.klines(setUpParams())).thenReturn(jsonArray);
        BinanceDownloader b = new BinanceDownloader(market);
        LinkedHashMap<String, Object> params = setUpParams();
        downloadedData = b.downloadKlines(params);
    }

}
