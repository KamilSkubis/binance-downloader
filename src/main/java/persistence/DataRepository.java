package persistence;

import model.Data;
import model.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    private final Logger logger;
    private final BatchWriter batchWriter;
    private final Writer writer;
    private final Reader reader;


    public DataRepository(Writer writer, BatchWriter batchWriter, Reader reader) {
        this.batchWriter = batchWriter;
        this.writer = writer;
        this.reader = reader;
        logger = LoggerFactory.getLogger(DataRepository.class);

    }


    public DataRepository(Writer writer, Reader reader) {
        this.writer = writer;
        this.reader = reader;
        this.batchWriter = null;
        logger = LoggerFactory.getLogger(DataRepository.class);
    }

    public List<Symbol> saveOrUpdateSymbols(List<String> symbolList) {
        List<Symbol> result = reader.getSymbols();

        List<String> onlySymbolNames = new ArrayList<>();

        for (Symbol s : result) {
            onlySymbolNames.add(s.getSymbolName());
        }

        List<String> differences = new ArrayList<>(symbolList);
        differences.removeAll(onlySymbolNames);


        if (differences.size() > 0) {
            logger.info("Found differences in Symbols table: " + differences);
            for (String s : differences) {
                Symbol symbol = new Symbol();
                symbol.setSymbolName(s);
                writer.write(symbol);
            }
            result = reader.getSymbols();
        }
        return result;
    }

    public List<Symbol> getSymbolsWithUSDT() {
        return reader.getSymbols();
    }

    public void sychronizeDownloadedSymbolsWithDb(List<String> downloadedSymbols) {
        List<Symbol> persistentSymbols = getSymbols();
        SymbolSynchronizator symbolSynchronizator = new SymbolSynchronizator(writer, reader);
        symbolSynchronizator.synchronizeUSDTSymbols(persistentSymbols, downloadedSymbols);
    }

    public List<Symbol> getSymbols() {
        return reader.getSymbols();
    }

    public LocalDateTime readLastDate(Symbol symbol) {
        return reader.readLastDate(symbol);
    }

    public void write(Symbol symbol) {
        writer.write(symbol);
    }

    public void write(List<Data> data) {
        if (data.size() == 0) {
            return;
        }
        var size = data.size();
        if (size >= 1000) {
            String symbol = data.get(0).getSymbol().getSymbolName();
            logger.info("Data for symbol: {} is too big, using batchWriter", symbol);
            long start = System.currentTimeMillis();
            batchWriter.write(data);
            long elapsed = System.currentTimeMillis() - start;
            logger.info("Writing {} rows of {} took {}", size, symbol, elapsed);
        } else {
            logger.info("Using hibernate to write data. Data size {}", data.size());
            writer.write(data);
        }
    }


    public List<Symbol> getSymbolsWithLastDate() {
        List<Symbol> symbols = getSymbols();
        for (Symbol symbol : symbols) {
            LocalDateTime dateFromPersistence = readLastDate(symbol);
            symbol.setLastDate(dateFromPersistence);
        }
        return symbols;


    }
}
