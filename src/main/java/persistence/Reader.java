package persistence;

import model.Symbol;

import java.time.LocalDateTime;
import java.util.List;

public interface Reader {

    List<Symbol> getSymbols();

    LocalDateTime readLastDate(Symbol symbol);
}
