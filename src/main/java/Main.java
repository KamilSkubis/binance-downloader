import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import com.google.gson.*;
import downloads.BinanceDownloader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        BinanceRunner binanceRunner = new BinanceRunner();
        binanceRunner.run();
    }

}
