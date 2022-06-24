import com.binance.connector.client.impl.SpotClientImpl;
import model.Data;
import downloads.BinanceDownloader;
import model.Symbol;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.MySQL;

import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl();
        BinanceDownloader binance = new BinanceDownloader(client.createMarket());

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "ETHUSDT");
        params.put("interval", "1d");
        params.put("limit", "5");
        List<Data> datas = binance.downloadKlines(params);




        Session s = MySQL.getSessionFactory().openSession();
        Query q = s.createQuery("From Symbol");
    }

}
