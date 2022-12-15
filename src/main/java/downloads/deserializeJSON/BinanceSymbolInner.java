package downloads.deserializeJSON;

public class BinanceSymbolInner {
    private String symbol;
    private transient int price;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
