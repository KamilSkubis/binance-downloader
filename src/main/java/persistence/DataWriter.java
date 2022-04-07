package persistence;

import downloads.BinanceData;

public interface DataWriter {

    public void writeData(BinanceData data);
}
