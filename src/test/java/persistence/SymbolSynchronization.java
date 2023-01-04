package persistence;

import model.Symbol;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SymbolSynchronization {
    private final Writer writer;
    private final Reader reader;

    public SymbolSynchronization(Writer writer, Reader reader) {
        this.writer = writer;
        this.reader = reader;
    }

    public void synchronize(List<Symbol> symbols, List<String> downloadedSymbols) {
        List<String> persistentSymbolNames = symbols.stream()
                .map(s -> s.getSymbolName())
                .collect(Collectors.toList());

        List<Symbol> filteredSymbols = downloadedSymbols.stream()
                .filter(Predicate.not(persistentSymbolNames::contains))
                .map(Symbol::new)
                .collect(Collectors.toList());

        filteredSymbols.stream().forEach(writer::write);
    }
}