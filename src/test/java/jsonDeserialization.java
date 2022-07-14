import com.google.gson.Gson;
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
                String y = "{\"data\":[{\"symbol\":\"BTC\",\"price\":\"100\"}],\"x-mbx-used-weight\":\"1\"}";

                String json = "{\"data\":[{\"symbol\":\"BTC\",\"price\":\"100\"}],\"x-mbx-used-weight\":\"10\"" +
                        ",\"x-mbx-used-weight-1m\":\"2\"}";
                Gson gson = new Gson();

                GsonSymbolOuter x = gson.fromJson(json, GsonSymbolOuter.class);

                assertEquals(1,x.symbolList.size());
                assertEquals("BTC",x.symbolList.get(0).getSymbol());
        }



}
