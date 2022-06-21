import com.binance.connector.client.impl.SpotClientImpl;
import downloads.BinanceBar;
import downloads.BinanceDownloader;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.HibernateUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl();
        BinanceDownloader binance = new BinanceDownloader(client.createMarket());

//        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("symbol", "BTCUSDT");
//        params.put("interval", "1d");
//        params.put("limit", "20");
//        List<BinanceBar> datas = binance.downloadKlines(params);

        List<BinanceBar> datas = new LinkedList<>();
        BinanceBar b = new BinanceBar();
        b.setTicker("test");
        b.setOpen(2313.03);
        b.setHigh(2323.00);
        b.setLow(2300.31);
        b.setClose(2312.23);
        b.setVolume(1000.00);
        b.setOpenTime(1231232l);
        datas.add(b);

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        for (BinanceBar data : datas) {
            s.persist(data);
        }
        t.commit();
        s.close();
    }

}
