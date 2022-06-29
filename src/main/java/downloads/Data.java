package downloads;

import java.util.Objects;

public class Data{

    String symbol;
    Long openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Double volume;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Data{" +
                "symbol='" + symbol + '\'' +
                ", openTime=" + openTime +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(symbol, data.symbol) && Objects.equals(openTime, data.openTime) && Objects.equals(open, data.open) && Objects.equals(high, data.high) && Objects.equals(low, data.low) && Objects.equals(close, data.close) && Objects.equals(volume, data.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, openTime, open, high, low, close, volume);
    }
}
