import com.google.gson.Gson;
import downloads.GsonTickerDataOuter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class jsonDeserialization {

        private final String jsonReturn = "{\"x-mbx-used-weight\":\"1\"}";

        @Test
        public void canDeserializedSimpleObject(){
                Gson gson = new Gson();
                GsonTickerDataOuter x = gson.fromJson(jsonReturn,GsonTickerDataOuter.class);
                assertEquals(1, x.usedWeight);
        }


}
