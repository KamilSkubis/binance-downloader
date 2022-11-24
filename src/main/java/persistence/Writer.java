package persistence;

import model.BinanceData;
import model.Symbol;

public interface Writer {

    void write(BinanceData data);
    void write(Symbol symbol);
}
