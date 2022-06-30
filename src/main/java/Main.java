import com.binance.connector.client.impl.SpotClientImpl;
import downloads.BinanceDownloader;
import model.Symbol;

public class Main {

    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl();
        BinanceDownloader binance = new BinanceDownloader(client.createMarket());

//        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("symbol", "ETHUSDT");
//        params.put("interval", "1d");
//        params.put("limit", "5");
//        List<Data> datas = binance.downloadKlines(params);

        Symbol symbol = new Symbol();
        symbol.setSymbolName("testTwoEntry");


    }

}
