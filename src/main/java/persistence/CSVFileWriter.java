package persistence;

import downloads.BinanceData;

import java.util.List;

public class CSVFileWriter implements DataWriter {

    public CSVFileWriter() {

    }

    @Override
    public void writeData(BinanceData data) {
        //create file named by ticker
//        File f = new File("B:\\" + data.getTicker() +".csv");

        //run loop to save data
        List<Long> openTime = data.getOpenTime();

    }
}
