package model;

import org.hibernate.annotations.Cascade;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

public class DataId implements Serializable {


    private static final long serialVersionUID = 8965453690374308907L;
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    Symbol symbol;

    @Column(name = "open_time", nullable = false)
    LocalDateTime openTime;

    public DataId() {
    }

    public DataId(Symbol symbol, LocalDateTime openTime) {
        this.symbol = symbol;
        this.openTime = openTime;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalDateTime openTime) {
        this.openTime = openTime;
    }
}
