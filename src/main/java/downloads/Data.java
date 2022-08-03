package downloads;

import model.Symbol;

public interface Data {

    Symbol getSymbol();

    void setSymbol(Symbol symbol);

    Long getOpenTime();

    void setOpenTime(Long openTime);

    Double getOpen();

    void setOpen(Double open);

    Double getHigh();

    void setHigh(Double high);

    Double getLow();

    void setLow(Double low);

    Double getClose();

    void setClose(Double close);

    Double getVolume();

    void setVolume(Double volume);

}
