import com.binance.connector.client.impl.SpotClientImpl;
import model.Data;
import downloads.BinanceDownloader;
import model.Ticker;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.HibernateUtil;

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

        List<Data> datas = new LinkedList<>();
        Ticker tx = new Ticker();
        tx.setTickerName("test2");
        Data b = new Data();
        b.setTicker(tx);
        b.setOpen(2313.03);
        b.setHigh(2323.00);
        b.setLow(2300.31);
        b.setClose(2312.23);
        b.setVolume(1000.00);
        b.setOpenTime(1231232l);
        datas.add(b);

        Ticker ty = new Ticker();
        ty.setTickerName("test2");
        Data c = new Data();
        c.setTicker(ty);
        c.setOpen(2313.03);
        c.setHigh(2323.00);
        c.setLow(2300.31);
        c.setClose(2312.23);
        c.setVolume(1000.00);
        c.setOpenTime(1231232l);
        datas.add(c);

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();

        for (Data data : datas) {
            if(data.getTicker().equals(s.getReference(Ticker.class,1))) {

            }

            s.persist(data);
        }
        t.commit();
        s.close();
    }

}
