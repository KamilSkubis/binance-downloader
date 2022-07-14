import com.google.gson.Gson;
import downloads.GsonSymbolInner;
import downloads.GsonSymbolOuter;
import org.junit.Test;

import static org.junit.Assert.*;

public class jsonDeserialization {

        private final String jsonReturn = "{\"x-mbx-used-weight\":\"1\"}";

        @Test
        public void canDeserializedSimpleObject(){
                Gson gson = new Gson();
                GsonSymbolOuter x = gson.fromJson(jsonReturn, GsonSymbolOuter.class);
                assertEquals(1, x.usedWeight);
        }

        @Test
        public void canDeserialize_array(){
                String json = "{\"data\":\"{\"symbol\":\"BTCUSDT\",\"price\":\"19854.74000000\"}\",\"x-mbx-used-weight\":\"1\",\"x-mbx-used-weight-1m\":\"1\"}";


                String iu = "{\"data\":\"[{\"symbol\":\"ETHBTC\",\"price\":\"0.05497500\"},{\"symbol\":\"LTCBTC\",\"price\":\"0.00245100\"},{\"symbol\":\"BNBBTC\",\"price\":\"0.01158200\"},{\"symbol\":\"NEOBTC\",\"price\":\"0.00306100\"}]\",\"x-mbx-used-weight\":\"2\",\"x-mbx-used-weight-1m\":\"2\"}";

                System.out.println(json);
                Gson gson = new Gson();

                GsonSymbolOuter x = gson.fromJson(iu, GsonSymbolOuter.class);
                GsonSymbolInner[] y = gson.fromJson(x.symbolList,GsonSymbolInner[].class);


                assertEquals(1,x.symbolList.length());
        }



}
