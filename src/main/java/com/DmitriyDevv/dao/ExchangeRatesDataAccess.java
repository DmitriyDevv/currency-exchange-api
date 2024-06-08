package com.DmitriyDevv.dao;

import com.DmitriyDevv.dto.Currency;
import com.DmitriyDevv.dto.ExchangeRate;
import com.DmitriyDevv.sql.DBManager;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDataAccess implements ActionsExchangeRates<ExchangeRate> {

    private final String sqlRequestAllExchangeRates =
            "SELECT tBase.ID as BaseID, tBase.Code as BaseCode, tBase.FullName as BaseFullName, tBase.Sign as BaseSign, "
                    + " tTarget.ID as TargetID, tTarget.Code as TargetCode, tTarget.FullName as TargetFullName, tTarget.Sign as TargetSign, tExch.ID, tExch.Rate "
                    + "FROM Currencies as tBase INNER JOIN ExchangeRates as tExch on tBase.ID = tExch.BaseCurrencyId "
                    + "                         INNER JOIN Currencies as tTarget on tTarget.ID = tExch.TargetCurrencyId ";

    @Override
    public List<ExchangeRate> getAll() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        DBManager.execute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sqlRequestAllExchangeRates);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Currency baseCurrency = getPrepareCurrency(rs, "BaseID", "BaseCode", "BaseFullName", "BaseSign");
                    Currency targetCurrency = getPrepareCurrency(rs, "TargetID", "TargetCode", "TargetFullName",
                            "TargetSign");

                    int ID = rs.getInt("Id");
                    BigDecimal rate = rs.getBigDecimal("Rate");

                    exchangeRates.add(new ExchangeRate(ID, baseCurrency, targetCurrency, rate));
                }
            }
        });
        return exchangeRates;
    }

    @Override
    public ExchangeRate getExchangeRateByCodes(Currency baseCurrency, Currency targetCurrency) throws SQLException {
        final ExchangeRate[] exchangeRate = {null};
        String sql = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        DBManager.execute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, baseCurrency.Id());
                ps.setInt(2, targetCurrency.Id());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int ID = rs.getInt("Id");
                        BigDecimal rate = rs.getBigDecimal("Rate");

                        exchangeRate[0] = new ExchangeRate(ID, baseCurrency, targetCurrency, rate);
                    }
                }
            }
        });
        return exchangeRate[0];
    }

    @Override
    public void addExchangeRate(ExchangeRate exchangeRate) {

    }

    private Currency getPrepareCurrency(ResultSet rs, String ID, String code, String fullName, String sign)
            throws SQLException {

        int baseID = rs.getInt(ID);
        String baseCode = rs.getString(code);
        String baseFullName = rs.getString(fullName);
        String baseSign = rs.getString(sign);

        return new Currency(baseID, baseCode, baseFullName, baseSign);
    }
}
