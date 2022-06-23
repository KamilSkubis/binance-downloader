package model;

//SQL query
//create table binance_1d(
//        id bigint AUTO_INCREMENT,
//        symbol_id int,
//        open_time bigint signed,
//        open double,
//        high double,
//        low double,
//        close double,
//        volume double,
//        key(id)
//        );


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "binance_1d")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Symbol symbol;

    @Column(name = "open_time", nullable = false)
    Long openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Double volume;

    public Data() {
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

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return openTime.equals(data.openTime) && open.equals(data.open) && high.equals(data.high) && low.equals(data.low) && close.equals(data.close) && volume.equals(data.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, open, high, low, close, volume);
    }

    public void setTicker(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}
