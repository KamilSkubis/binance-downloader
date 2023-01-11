package model;

//SQL query
//create table binance_data(
//        id bigint AUTO_INCREMENT,
//        symbol_id int,
//        open_time datetime(6),
//        open double,
//        high double,
//        low double,
//        close double,
//        volume double,
//        key(id)
//        );


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "binance_data")
public class BinanceData implements Data {

    @EmbeddedId
    private final DataId dataId;

    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;

    public BinanceData() {
        dataId = new DataId();
    }


    public LocalDateTime getOpenTime() {
        return dataId.getOpenTime();
    }

    public void setOpenTime(LocalDateTime openTime) {
        this.dataId.setOpenTime(openTime);
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

    public void setTicker(Symbol symbol) {
        this.dataId.setSymbol(symbol);
    }

    public Symbol getSymbol() {
        return dataId.getSymbol();
    }

    public void setSymbol(Symbol symbol) {
        this.dataId.setSymbol(symbol);
    }

    public DataId getDataId() {
        return dataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinanceData that = (BinanceData) o;
        return Objects.equals(dataId, that.dataId) && Objects.equals(open, that.open) && Objects.equals(high, that.high) && Objects.equals(low, that.low) && Objects.equals(close, that.close) && Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId.hashCode(), open, high, low, close, volume);
    }

    @Override
    public String toString() {
        return "BinanceData{" +
                "dataId=" + dataId +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
