package com.DmitriyDevv.service;

import com.DmitriyDevv.dao.CurrenciesDataAccess;
import com.DmitriyDevv.dto.Currency;
import com.DmitriyDevv.exceptions.RequestException;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

public class CurrenciesService {

    private static final CurrenciesDataAccess currencys = new CurrenciesDataAccess();

    public static List<Currency> getCurrencyList() throws SQLException {
        return currencys.getAll();
    }

    public static Currency getCurrencyByCode(String code) throws SQLException {
        if (code.isEmpty()) {
            throw new RequestException("The currency code is missing", HttpServletResponse.SC_BAD_REQUEST);
        }

        Currency currency = currencys.getCurrencyByCode(code);

        if (currency == null) {
            throw new RequestException("The currency was not found", HttpServletResponse.SC_NOT_FOUND);
        }

        return currency;
    }
}
