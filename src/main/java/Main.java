import com.binance.connector.client.impl.SpotClientImpl;
import model.Data;
import downloads.BinanceDownloader;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.HibernateUtil;

import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl();
        BinanceDownloader binance = new BinanceDownloader(client.createMarket());

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "BTCUSDT");
        params.put("interval", "1d");
        params.put("limit", "20");
        List<Data> datas = binance.downloadKlines(params);

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        // fetch symbol list
        Query query = s.createQuery("from symbols");

        List symbols = query.getResultList();

        System.out.println("data length to save: " + datas.size());

        for (Data data : datas) {



            s.persist(data);
        }
        t.commit();
        s.close();
    }

}
