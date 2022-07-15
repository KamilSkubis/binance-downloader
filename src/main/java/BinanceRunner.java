import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import downloads.BinanceDownloader;

import java.util.List;

public class BinanceRunner {

    public void run(){
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market =client.createMarket();
        BinanceDownloader binance = new BinanceDownloader(market);


    }

}
