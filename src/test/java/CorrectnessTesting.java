import model.BinanceData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import persistence.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class CorrectnessTesting {

    /*
    This test will check if openTime of downloaded and persisted data is within 1d
    works only when settings.properties in target has timeframe set to 1d
     */
    @Ignore
    @Test
    public void testCorrectness() {
        SessionFactory sf = MySQLUtilTesting.getSessionFactory();
        Writer writer = new DbWriter(sf);
        Reader reader = new DbReader(sf);
        DataRepository dr = new DataRepository(writer, reader);

        Session session = sf.openSession();
        session.beginTransaction();

        List<BinanceData> resultList = session.createQuery("From BinanceData", BinanceData.class).getResultList();
        System.out.println(resultList.size());
        List<BinanceData> btcusdt = resultList.stream()
                .filter(d -> Objects.equals(d.getSymbol().getSymbolName(), "BTCUSDT"))
                .sorted(Comparator.comparing(BinanceData::getOpenTime))
                .collect(Collectors.toList());

        System.out.println(btcusdt);


        for (int i = 1; i < btcusdt.size(); i++) {
            var d1 = btcusdt.get(i - 1);
            var d2 = btcusdt.get(i);

            if (!d1.getOpenTime().plusDays(1).equals(d2.getOpenTime())) {
                System.out.println("WARNING!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(d1.getOpenTime().plusDays(1) + " !!!!!! " + d2.getOpenTime());
                System.out.println(d1.getSymbol().getSymbolName() + "   " + d2.getSymbol().getSymbolName());
                fail();
            }
            System.out.println(d1.getOpenTime().plusDays(1) + " == " + d2.getOpenTime());
        }


    }
}
