package persistence;

import model.Data;
import model.Symbol;

public interface Writer {

    void write(Data data);
    void write(Symbol symbol);
}
