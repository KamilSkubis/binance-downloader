package model;

import org.hibernate.annotations.Cascade;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class DataId implements Serializable {


    private static final long serialVersionUID = 8965453690374308907L;
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    Symbol symbol;

    @Column(name = "open_time", nullable = false)
    LocalDateTime openTime;

    public DataId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataId dataId = (DataId) o;
        return Objects.equals(symbol, dataId.symbol) && Objects.equals(openTime, dataId.openTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, openTime);
    }
}
