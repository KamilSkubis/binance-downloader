package persistence;

import model.Symbol;

import java.util.List;

public interface Reader {

    List<Symbol> getSymbols();
}
