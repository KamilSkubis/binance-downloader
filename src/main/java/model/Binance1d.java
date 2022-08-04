package model;

//SQL query
//create table binance_1d(
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


import downloads.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "binance_1d")
public class Binance1d implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    public Binance1d() {
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
        Binance1d binance1d = (Binance1d) o;
        return openTime.equals(binance1d.openTime) && open.equals(binance1d.open) && high.equals(binance1d.high) && low.equals(binance1d.low) && close.equals(binance1d.close) && volume.equals(binance1d.volume);
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
