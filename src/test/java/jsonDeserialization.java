import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
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
                Gson gson = new Gson();
                GsonSymbolOuter gs = new GsonSymbolOuter();
                gs.symbolList = "[{\"symbol\":\"213\"}]";
                gs.usedWeight = 1;
                gs.usedWeight1m = 0;

                String j = new Gson().toJson(gs);

                System.out.println(j);
                GsonSymbolOuter x = gson.fromJson(j, GsonSymbolOuter.class);
                GsonSymbolInner[] y = gson.fromJson(x.symbolList,GsonSymbolInner[].class);
                assertEquals(1,y.length);
        }



}
