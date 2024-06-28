package com.DmitriyDevv.dao;

import com.DmitriyDevv.dto.CurrencyDto;
import com.DmitriyDevv.sql.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDataAccess {

    public List<CurrencyDto> getAll() throws SQLException {
        List<CurrencyDto> currencies = new ArrayList<>();
        String sql = "SELECT ID, Code, FullName, Sign FROM Currencies";

        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("Id");
                            String code = rs.getString("Code");
                            String fullName = rs.getString("FullName");
                            String sign = rs.getString("Sign");
                            currencies.add(new CurrencyDto(id, fullName, code, sign));
                        }
                    }
                });
        return currencies;
    }

    public CurrencyDto getCurrencyByCode(String code) throws SQLException {
        final CurrencyDto[] currencyDto = {null};
        String sql = "SELECT ID, Code, FullName, Sign " + "FROM Currencies " + "WHERE Code = ?";

        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, code);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                int id = rs.getInt("Id");
                                String getCode = rs.getString("Code");
                                String fullName = rs.getString("FullName");
                                String sign = rs.getString("Sign");
                                currencyDto[0] = (new CurrencyDto(id, fullName, getCode, sign));
                            }
                        }
                    }
                });
        return currencyDto[0];
    }

    public void addCurrency(CurrencyDto currencyDto) throws SQLException {
        String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, currencyDto.code());
                        ps.setString(2, currencyDto.name());
                        ps.setString(3, currencyDto.sign());
                        ps.executeUpdate();
                    }
                });
    }
}
