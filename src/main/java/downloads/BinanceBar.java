package downloads;

//SQL query
//create table data(
//id bigint signed not null auto_increment,
//ticker varchar(10),
//open_time bigint signed,
//open double,
//high double,
//low double,
//close double,
//volume double,
//close_time bigint signed,
//quote_asset double,
//number_trades int,
//taker_buy_base double,
//taker_buy_quote double
//);







public class BinanceBar {

    String ticker;
    Long openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Double volume;
    Long closeTime;
    Double quoteAsset;
    Integer numberOfTrades;
    Double takerBuyBase;
    Double takerBuyQuote;

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

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public Double getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(Double quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public Integer getNumberOfTrades() {
        return numberOfTrades;
    }

    public void setNumberOfTrades(Integer numberOfTrades) {
        this.numberOfTrades = numberOfTrades;
    }

    public Double getTakerBuyBase() {
        return takerBuyBase;
    }

    public void setTakerBuyBase(Double takerBuyBase) {
        this.takerBuyBase = takerBuyBase;
    }

    public Double getTakerBuyQuote() {
        return takerBuyQuote;
    }

    public void setTakerBuyQuote(Double takerBuyQuote) {
        this.takerBuyQuote = takerBuyQuote;
    }
}
