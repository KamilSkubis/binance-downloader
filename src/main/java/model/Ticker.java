package model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="tickers")
public class Ticker {

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
