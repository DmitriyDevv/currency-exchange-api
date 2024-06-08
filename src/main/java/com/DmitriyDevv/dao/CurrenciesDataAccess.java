package com.DmitriyDevv.dao;

import com.DmitriyDevv.dto.Currency;
import com.DmitriyDevv.sql.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDataAccess implements ActionsCurrencies<Currency> {

    @Override
    public List<Currency> getAll() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT ID, Code, FullName, Sign FROM Currencies";

        DBManager.execute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String code = rs.getString("Code");
                    String fullName = rs.getString("FullName");
                    String sign = rs.getString("Sign");
                    currencies.add(new Currency(id, fullName, code, sign));
                }
            }
        });
        return currencies;
    }

    @Override
    public Currency getCurrencyByCode(String code) throws SQLException {
        final Currency[] currency = {null};
        String sql = "SELECT ID, Code, FullName, Sign "
                + "FROM Currencies "
                + "WHERE Code = ?";

        DBManager.execute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, code);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("Id");
                        String getCode = rs.getString("Code");
                        String fullName = rs.getString("FullName");
                        String sign = rs.getString("Sign");
                        currency[0] = (new Currency(id, fullName, getCode, sign));
                    }
                }
            }
        });
        return currency[0];
    }

    @Override
    public void addCurrency(Currency currency) throws SQLException {
        String sql = "INSERT INTO Currencies (ID, Code, FullName, Sign) VALUES (?, ?, ?, ?)";
        DBManager.execute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, currency.Id());
                ps.setString(2, currency.code());
                ps.setString(3, currency.name());
                ps.setString(4, currency.sign());
                ps.execute();
            }
        });
    }
}
