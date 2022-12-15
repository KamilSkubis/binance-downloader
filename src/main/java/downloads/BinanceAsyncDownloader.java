package downloads;

import com.binance.connector.client.impl.spot.Market;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinanceAsyncDownloader {

    private final Market market;
    private final Logger logger;

    private final int usedWeight;
    private final int usedWeight1m;

    public BinanceAsyncDownloader(Market market) {
        logger = LoggerFactory.getLogger(BinanceDownloader.class);
        this.market = market;
        usedWeight = 0;
        usedWeight1m = 0;


    }


}
