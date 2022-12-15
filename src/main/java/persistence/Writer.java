package persistence;

import model.Data;
import model.Symbol;

import java.util.List;

public interface Writer {

    void write(Data data);

    void write(List<Data> data);

    void write(Symbol symbol);
}
