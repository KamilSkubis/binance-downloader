import com.google.gson.Gson;
import downloads.deserializeJSON.BinanceSymbolInner;
import downloads.deserializeJSON.BinanceSymbolOuter;
import org.junit.Test;

import static org.junit.Assert.*;

public class jsonDeserialization {

        private final String jsonReturn = "{\"x-mbx-used-weight\":\"1\"}";

        @Test
        public void canDeserializedSimpleObject(){
                Gson gson = new Gson();
                BinanceSymbolOuter x = gson.fromJson(jsonReturn, BinanceSymbolOuter.class);
                assertEquals(1, x.usedWeight);
        }

        @Test
        public void canDeserialize_array(){
                Gson gson = new Gson();
                BinanceSymbolOuter gs = new BinanceSymbolOuter();
                gs.symbolList = "[{\"symbol\":\"213\"}]";
                gs.usedWeight = 1;
                gs.usedWeight1m = 0;

                String j = new Gson().toJson(gs);

                System.out.println(j);
                BinanceSymbolOuter x = gson.fromJson(j, BinanceSymbolOuter.class);
                BinanceSymbolInner[] y = gson.fromJson(x.symbolList, BinanceSymbolInner[].class);
                assertEquals(1,y.length);
        }



}
