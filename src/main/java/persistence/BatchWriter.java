package persistence;

import config.Config;
import model.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BatchWriter {


    private final String url;
    private final String login;
    private final String password;

    public BatchWriter(Config config) {
        url = config.getUrl() + "?rewriteBatchedStatements=true";
        login = config.getLogin();
        password = config.getPassword();
    }

    public void write(List<Data> data) {

        Connection con = null;
        try {
            con = DriverManager.getConnection(url, login, password);
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(
                    "Insert into test.binance_data(open_time,close,high,low,open,volume,symbol_id) values(?,?,?,?,?,?,?)");

            long start = System.currentTimeMillis();

            for (Data d : data) {

                var formatedDate = d.getOpenTime().format(DateTimeFormatter.ISO_DATE_TIME);

                ps.setString(1, formatedDate);
                ps.setDouble(2, d.getClose());
                ps.setDouble(3, d.getHigh());
                ps.setDouble(4, d.getLow());
                ps.setDouble(5, d.getOpen());
                ps.setDouble(6, d.getVolume());
                ps.setLong(7, d.getDataId().getSymbol().getId());

                ps.addBatch();
                ps.clearParameters();

            }

            ps.executeLargeBatch();

            con.commit();
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("elapsed time: " + elapsed);
            con.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
