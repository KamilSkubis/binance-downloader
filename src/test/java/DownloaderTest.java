import com.binance.connector.client.impl.spot.Market;
import downloads.BinanceData;
import downloads.BinanceDownloader;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.LinkedList;

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

    private final String jsonReturn = "[{" + "\"symbol\":" + "\"ticker\"," + "\"price\":" + "\"100\"" + "}]";
    private final String jsonArray = "[[1499040000000,\"0.01634790\",\"0.80000000\",\"0.01575800\",\"0.01577100\",\"148976.11427815\",1499644799999,\"2434.19055334\",308,\"1756.87402397\",\"28.46694368\",\"17928899.62484339\" ]]";


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
    public void downloadKlines_ReturnCorrectDataFromJsonArray() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(1499040000000L, data.getLastOpenTime(1), 1);

    }
    
    @Test
    public void downloadKlines_ReturnCorrectOpenPrice() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(0.01634790, data.getLastOpen(1), 0.00001);
    }


    @Test
    public void downloadKlines_ReturnCorrectHighPrice() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(0.80000000, data.getLastHigh(1), 0.00001);
    }

    @Test
    public void downloadKlines_ReturnCorrectLowPrice() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(0.01575800, data.getLastLow(1), 0.00001);
    }


    @Test
    public void downloadKlines_ReturnCorrectClosePrice() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(0.01577100, data.getLastClose(1), 0.00001);
    }

    @Test
    public void downloadKlines_ReturnCorrectVolume() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(148976.11427815, data.getLastVolume(1), 0.00001);
    }


    @Test
    public void downloadKlines_ReturnCorrectCloseTime() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(1499644799999L, data.getLastCloseTime(1), 1);
    }

    @Test
    public void downloadKlines_ReturnCorrectQuoteAssetVolume() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(2434.19055334, data.getLastQuoteAsset(1), 0.00001);
    }

    @Test
    public void downloadKlines_ReturnCorrectNumberOfTrades() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(308, data.getLastNumberTrades(1));
    }

    @Test
    public void downloadKlines_ReturnCorrectTakerBuyBase() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(1756.87402397, data.getLastTakerBuyBase(1), 0.00001);
    }

    @Test
    public void downloadKlines_ReturnCorrect_TakerBuyQuote() {
        BinanceData data = getDataFromJsonTestArray();
        assertEquals(28.46694368, data.getLastTakerBuyQuote(1), 0.00001);
    }


    @NotNull
    private LinkedHashMap<String, Object> setUpParams() {
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("symbol", "test");
        params.put("interval", "1d");
        params.put("limit", 1);
        return params;
    }

    private BinanceData getDataFromJsonTestArray() {
        Market market = Mockito.mock(Market.class);
        Mockito.when(market.klines(setUpParams())).thenReturn(jsonArray);
        BinanceDownloader b = new BinanceDownloader(market);
        LinkedHashMap<String, Object> params = setUpParams();
        BinanceData data = b.downloadKlines(params);
        return data;
    }

}
