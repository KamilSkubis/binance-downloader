import config.ConfigReader;

public class Main {

    public static void main(String[] args) {

        ConfigReader configReader = new ConfigReader();
        System.out.println(configReader.getUrl());
        System.out.println(configReader.getPassword());
        System.out.println(configReader.getLogin());
//
//        BinanceRunner binanceRunner = new BinanceRunner();
//        binanceRunner.run();
    }

}
