package downloads;

import java.util.ArrayList;
import java.util.List;

public class BinanceData {

    String ticker;
    List<Long> openTime;
    List<Double> open;
    List<Double> high;
    List<Double> low;
    List<Double> close;
    List<Double> volume;
    List<Long> closeTime;
    List<Double> quoteAsset;
    List<Integer> numberOfTrades;
    List<Double> takerBuyBase;
    List<Double> takerBuyQuote;


    public BinanceData(){
        String ticker = null;
        openTime = new ArrayList<>();
        open = new ArrayList<>();
        high = new ArrayList<>();
        low = new ArrayList<>();
        close = new ArrayList<>();
        volume = new ArrayList<Double>();
        closeTime = new ArrayList<>();
        quoteAsset = new ArrayList<>();
        numberOfTrades = new ArrayList<>();
        takerBuyBase = new ArrayList<>();
        takerBuyQuote = new ArrayList<>();

    }

    public void setTicker(String symbol) {
        this.ticker = symbol;
    }

    public void pushToOpenTime(Long openTime) {
        this.openTime.add(openTime);
    }

    public String getTicker() {
        return this.ticker;
    }

    public void pushOpen(double data) {
        this.open.add(data);
    }

    public void pushHigh(double data) {
        this.high.add(data);
    }

    public void pushLow(double data) {
        this.low.add(data);
    }

    public void pushClose(double data) {
        this.close.add(data);
    }

    public void pushVolume(double data) {
        this.volume.add(data);
    }

    public void pushCloseTime(Long data) {
        this.closeTime.add(data);
    }

    public void pushQuoteAsset(double data) {
        this.quoteAsset.add(data);
    }

    public void pushNumberTrades(int data) {
        this.numberOfTrades.add(data);
    }

    public void pushTakerBuyBase(double data) {
        this.takerBuyBase.add(data);
    }

    public void pushTakerBuyQuote(double data) {
        this.takerBuyQuote.add(data);
    }

    public List<Long> getOpenTime() {
        return openTime;
    }

    public List<Double> getOpen() {
        return open;
    }

    public List<Double> getHigh() {
        return high;
    }

    public List<Double> getLow() {
        return low;
    }

    public List<Double> getClose() {
        return close;
    }

    public List<Double> getVolume() {
        return volume;
    }

    public List<Long> getCloseTime() {
        return closeTime;
    }

    public List<Double> getQuoteAsset() {
        return quoteAsset;
    }

    public List<Integer> getNumberTrades() {
        return numberOfTrades;
    }

    public List<Double> getTakerBuyBase() {
        return takerBuyBase;
    }

    public List<Double> getTakeBuyQuote() {
        return takerBuyQuote;
    }

    public double getLastOpen(int i) {
        return open.get(open.size()-i);
    }

    public double getLastHigh(int i) {
        return high.get(high.size()-i);
    }

    public double getLastLow(int i) {
        return low.get(low.size()-i);
    }

    public double getLastClose(int i) {
        return close.get(close.size()-i);
    }


    public Double getLastVolume(int i) {
        return volume.get(volume.size()-i);
    }


    public double getLastQuoteAsset(int i) {
        return quoteAsset.get(quoteAsset.size()-i);
    }

    public double getLastTakerBuyBase(int i) {
        return takerBuyBase.get(takerBuyBase.size()-i);
    }


    public double getLastTakerBuyQuote(int i) {
        return takerBuyQuote.get(takerBuyQuote.size()-i);

    }

    public Long getLastOpenTime(int i) {
        return openTime.get(openTime.size()-i);
    }

    public Long getLastCloseTime(int i) {
        return closeTime.get(closeTime.size()-i);
    }

    public int getLastNumberTrades(int i) {
        return numberOfTrades.get(numberOfTrades.size()-i);
    }
}
