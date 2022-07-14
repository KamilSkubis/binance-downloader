import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import com.google.gson.*;
import downloads.BinanceDownloader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true);
        Market market =client.createMarket();
//        BinanceDownloader binance = new BinanceDownloader(market);
//        List<String> r= binance.getTickers();
//        System.out.println(r.toString());





        String o = market.tickerSymbol(null);
        System.out.println(o);
    }

}
