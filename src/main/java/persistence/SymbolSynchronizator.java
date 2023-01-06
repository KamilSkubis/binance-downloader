package persistence;

import model.Symbol;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SymbolSynchronizator {
    private final Writer writer;
    private final Reader reader;

    public SymbolSynchronizator(Writer writer, Reader reader) {
        this.writer = writer;
        this.reader = reader;
    }

    public void synchronizeUSDTSymbols(List<Symbol> symbols, List<String> downloadedSymbols) {
        List<String> persistentSymbolNames = symbols.stream()
                .map(Symbol::getSymbolName)
                .collect(Collectors.toList());

        List<Symbol> filteredSymbols = downloadedSymbols.stream()
                .filter(s -> s.endsWith("USDT"))
                .filter(Predicate.not(persistentSymbolNames::contains))
                .map(Symbol::new)
                .collect(Collectors.toList());

         filteredSymbols.forEach(writer::write);
    }
}