import config.Config;
import config.ConfigLocation;
import config.ConfigReader;

public class Main {

    public static void main(String[] args) {

        ConfigLocation configLocation = new ConfigLocation();
        ConfigReader configReader = new ConfigReader();
        Config config = configReader.read(configLocation);




        BinanceRunner binanceRunner = new BinanceRunner();
        binanceRunner.run();
    }

}
