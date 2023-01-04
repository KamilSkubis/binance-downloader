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
        //convert List<Symbol> to List<String>
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

    public List<Symbol> getSymbolsToDownload(List<String> symbolList) {
        List<Symbol> result = new ArrayList<>();
        saveOrUpdateSymbols(symbolList);

        return result;
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
        writer.write(data);
    }


}
