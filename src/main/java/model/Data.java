package model;

import java.time.LocalDateTime;

public interface Data {

    Symbol getSymbol();

    void setSymbol(Symbol symbol);

    LocalDateTime getOpenTime();

    void setOpenTime(LocalDateTime openTime);

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

    void setId(Long id);

    long getId();
}
