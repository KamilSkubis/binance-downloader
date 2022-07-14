package downloads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonSymbolOuter {

    @SerializedName("data")
    public String symbolList;

    @SerializedName("x-mbx-used-weight")
    public int usedWeight;

    @SerializedName("x-mbx-used-weight-1m")
    public int usedWeight1m;

}
