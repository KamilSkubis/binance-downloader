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


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "binance_data")
public class BinanceData implements Data {
//
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    Long id;

    @Id
    Long id;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    Symbol symbol;

    @Column(name = "open_time", nullable = false)
    LocalDateTime openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Double volume;

    public BinanceData() {
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalDateTime openTime) {
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
        BinanceData binanceData = (BinanceData) o;
        return openTime.equals(binanceData.openTime) && open.equals(binanceData.open) && high.equals(binanceData.high) && low.equals(binanceData.low) && close.equals(binanceData.close) && volume.equals(binanceData.volume);
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
