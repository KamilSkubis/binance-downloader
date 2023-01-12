import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import downloads.BinanceDownloader;
import downloads.Downloader;
import model.Symbol;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class downloadTest {

    @Test
    public void downloadAllTickers(){
        SpotClientImpl client = new SpotClientImpl();
        client.setShowLimitUsage(true); //important option to enable
        Market market = client.createMarket();
        Downloader downloader = new BinanceDownloader(market);


        List<String> tickers = downloader.getTickers();
        System.out.println(tickers.size());
        System.out.println(tickers.toString());

        List<String> filteredSymbols = tickers.stream()
                .filter(s -> s.endsWith("USDT"))
                .filter(Predicate.not(s -> s.contains("UP")))
                .filter(Predicate.not(s -> s.contains("DOWN")))
                .collect(Collectors.toList());

        System.out.println(filteredSymbols.size());
        System.out.println(filteredSymbols);
    }
}
