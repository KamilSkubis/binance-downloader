package downloads.deserializeJSON;

import com.google.gson.annotations.SerializedName;

public class BinanceSymbolOuter {

    @SerializedName("data")
    public String symbolList;

    @SerializedName("x-mbx-used-weight")
    public int usedWeight;

    @SerializedName("x-mbx-used-weight-1m")
    public int usedWeight1m;

}
