package model;

import javax.persistence.*;

@Entity
@Table(name="tickers")
public class Ticker {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="ticker_name")
    private String tickerName;

    public Ticker() {
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "tickerName='" + tickerName + '\'' +
                '}';
    }
}
