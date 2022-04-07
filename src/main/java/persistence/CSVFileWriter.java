package persistence;

import downloads.BinanceData;

import java.io.File;

public class CSVFileWriter implements DataWriter {

    public CSVFileWriter(){

    }

    @Override
    public void writeData(BinanceData data) {
        //create file named by ticker
//        File f = new File("B:\\" + data.getTicker() +".csv");

        //run loop to save data
        String openTime= data.getOpenTime();

    }
}
