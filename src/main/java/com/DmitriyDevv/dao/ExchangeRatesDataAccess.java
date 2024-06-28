package com.DmitriyDevv.dao;

import com.DmitriyDevv.dto.CurrencyDto;
import com.DmitriyDevv.dto.ExchangeRateDto;
import com.DmitriyDevv.sql.DBManager;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDataAccess {

    private final String sqlRequestAllExchangeRates =
            "SELECT tBase.ID as BaseID, tBase.Code as BaseCode, tBase.FullName as BaseFullName, tBase.Sign as BaseSign, "
                    + " tTarget.ID as TargetID, tTarget.Code as TargetCode, tTarget.FullName as TargetFullName, tTarget.Sign as TargetSign, tExch.ID, tExch.Rate "
                    + "FROM Currencies as tBase INNER JOIN ExchangeRates as tExch on tBase.ID = tExch.BaseCurrencyId "
                    + "                         INNER JOIN Currencies as tTarget on tTarget.ID = tExch.TargetCurrencyId ";

    public void updateRate(int baseCurrencyId, int targetCurrencyId, BigDecimal rate)
            throws SQLException {
        String sql =
                "UPDATE ExchangeRates SET rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setBigDecimal(1, rate);
                        ps.setInt(2, baseCurrencyId);
                        ps.setInt(3, targetCurrencyId);
                        ps.executeUpdate();
                    }
                });
    }

    public List<ExchangeRateDto> getAll() throws SQLException {
        List<ExchangeRateDto> exchangeRateDtos = new ArrayList<>();

        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sqlRequestAllExchangeRates);
                            ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            CurrencyDto baseCurrencyDto =
                                    getPrepareCurrency(
                                            rs, "BaseID", "BaseCode", "BaseFullName", "BaseSign");
                            CurrencyDto targetCurrencyDto =
                                    getPrepareCurrency(
                                            rs,
                                            "TargetID",
                                            "TargetCode",
                                            "TargetFullName",
                                            "TargetSign");

                            int ID = rs.getInt("Id");
                            BigDecimal rate = rs.getBigDecimal("Rate");

                            exchangeRateDtos.add(
                                    new ExchangeRateDto(
                                            ID, baseCurrencyDto, targetCurrencyDto, rate));
                        }
                    }
                });
        return exchangeRateDtos;
    }

    public ExchangeRateDto getExchangeRateForPair(
            CurrencyDto baseCurrencyDto, CurrencyDto targetCurrencyDto) throws SQLException {
        final ExchangeRateDto[] exchangeRateDto = {null};
        String sql =
                "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, baseCurrencyDto.Id());
                        ps.setInt(2, targetCurrencyDto.Id());
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                int ID = rs.getInt("Id");
                                BigDecimal rate = rs.getBigDecimal("Rate");

                                exchangeRateDto[0] =
                                        new ExchangeRateDto(
                                                ID, baseCurrencyDto, targetCurrencyDto, rate);
                            }
                        }
                    }
                });
        return exchangeRateDto[0];
    }

    public void addExchangeRate(int baseCurrencyId, int targetCurrencyId, BigDecimal rate)
            throws SQLException {
        String sql =
                "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";

        DBManager.execute(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, baseCurrencyId);
                        ps.setInt(2, targetCurrencyId);
                        ps.setBigDecimal(3, rate);
                        ps.executeUpdate();
                    }
                });
    }

    private CurrencyDto getPrepareCurrency(
            ResultSet rs, String ID, String code, String fullName, String sign)
            throws SQLException {

        int baseID = rs.getInt(ID);
        String baseCode = rs.getString(code);
        String baseFullName = rs.getString(fullName);
        String baseSign = rs.getString(sign);

        return new CurrencyDto(baseID, baseFullName, baseCode, baseSign);
    }
}
