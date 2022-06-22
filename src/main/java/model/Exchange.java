package model;

public class Exchange {

    String exchangeName;

    public Exchange() {
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchangeName='" + exchangeName + '\'' +
                '}';
    }
}
