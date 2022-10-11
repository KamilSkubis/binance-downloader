package downloads;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;

public class BinanceAsyncDownloader {

    private final Market market;
    private final Logger logger;

    private int usedWeight;
    private int usedWeight1m;

    public BinanceAsyncDownloader(Market market) {
        logger = LoggerFactory.getLogger(BinanceDownloader.class);
        this.market = market;
        usedWeight = 0;
        usedWeight1m = 0;


    }








}
