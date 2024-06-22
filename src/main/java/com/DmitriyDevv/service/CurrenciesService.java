package com.DmitriyDevv.service;

import com.DmitriyDevv.dao.CurrenciesDataAccess;
import com.DmitriyDevv.dto.Currency;
import com.DmitriyDevv.exceptions.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import org.sqlite.SQLiteErrorCode;

import java.sql.SQLException;
import java.util.List;

public class CurrenciesService {

    private static final CurrenciesDataAccess currencies = new CurrenciesDataAccess();

    public static List<Currency> getCurrencyList() throws SQLException {
        return currencies.getAll();
    }

    public static Currency getCurrencyByCode(String code) throws SQLException {
        if (code.isEmpty()) {
            throw new RequestException(
                    "The currency code is missing", HttpServletResponse.SC_BAD_REQUEST);
        }

        Currency currency = currencies.getCurrencyByCode(code);

        if (currency == null) {
            throw new RequestException(
                    "The currency was not found", HttpServletResponse.SC_NOT_FOUND);
        }

        return currency;
    }

    public static void addCurrency(String name, String code, String sign) throws SQLException {
        if (name.isEmpty() || code.isEmpty() || sign.isEmpty()) {
            throw new RequestException(
                    "The required form field is missing", HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            Currency newCurrency = new Currency(0, name, code, sign);
            currencies.addCurrency(newCurrency);
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new RequestException(
                        "A currency with this code already exists",
                        HttpServletResponse.SC_CONFLICT);
            } else {
                throw new SQLException();
            }
        }
    }
}
