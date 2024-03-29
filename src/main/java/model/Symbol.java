package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "symbols")
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @Transient
    private LocalDateTime lastDate;

    public Symbol() {
    }


    public Symbol(String symbol, LocalDateTime lastDate) {
        this.symbol = symbol;
        this.lastDate = lastDate;
    }

    public Symbol(String symbol) {
        this.symbol = symbol;
        this.lastDate = LocalDateTime.of(2010, 1, 1, 0, 0, 0);
    }

    public String getSymbolName() {
        return symbol;
    }

    public void setSymbolName(String symbol) {
        this.symbol = symbol;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDateTime lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return symbol.equals(symbol1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", lastDate=" + lastDate +
                '}';
    }
}
