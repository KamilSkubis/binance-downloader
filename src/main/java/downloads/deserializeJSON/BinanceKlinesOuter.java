package downloads.deserializeJSON;

import com.google.gson.annotations.SerializedName;

public class BinanceKlinesOuter {

    @SerializedName("data")
    public String data;

    @SerializedName("x-mbx-used-weight")
    public int usedWeight;

    @SerializedName("x-mbx-used-weight-1m")
    public int usedWeight1m;

}
