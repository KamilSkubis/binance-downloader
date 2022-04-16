import downloads.BinanceData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BinanceDataTest {

    BinanceData binanceData;

    @Before
    public void startUp(){
        binanceData = new BinanceData();
    }

    @Test
    public void canSetUpTicker(){
        binanceData.setTicker("test");
        Assert.assertEquals("test", binanceData.getTicker());
    }

    @Test
    public void getOpenTime_withoutData_returnemptyList(){
        binanceData.getOpenTime();
        Assert.assertEquals(0, binanceData.getOpenTime().size());
    }

    @Test
    public void getHigh_withoutData_returnEmptyList(){
        Assert.assertEquals(0,binanceData.getHigh().size());
    }

    @Test
    public void getOpen_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getOpen().size());
    }

    @Test
    public void getLow_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getLow().size());
    }

    @Test
    public void getClose_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getClose().size());
    }

    @Test
    public void getVolume_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getVolume().size());
    }

    @Test
    public void getCloseTime_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getCloseTime().size());
    }

    @Test
    public void getQuoteAsset_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getQuoteAsset().size());
    }

    @Test
    public void getNumberOfTrades_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getNumberTrades().size());
    }

    @Test
    public void getTakerBuyBase_withoutData_returnEmptyList(){
        Assert.assertEquals(0, binanceData.getTakerBuyBase().size());
    }

    @Test
    public void getTakerBuyQuote_withoutData_returnEmptyList(){
        Assert.assertEquals(0,binanceData.getTakeBuyQuote().size());
    }
}
