package downloads;

import model.Data;
import model.Symbol;

import java.util.LinkedHashMap;
import java.util.List;

public interface Downloader {
    List<String> getTickers();

    List<Data> downloadKlines(LinkedHashMap<String, Object> params, List<Symbol> symbols);
}
