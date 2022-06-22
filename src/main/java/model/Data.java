package model;

//SQL query
//create table binance_data(
//        id bigint AUTO_INCREMENT,
//        ticker_id varchar(10),
//        open_time bigint signed,
//        open double,
//        high double,
//        low double,
//        close double,
//        volume double,
//        key(id)
//        );


import javax.persistence.*;

@Entity
@Table(name = "binance_data")
public class Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(name="ticker_id")
    String ticker;

    @Column(name = "open_time", nullable = false)
    Long openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Double volume;

    public Data() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
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

}
