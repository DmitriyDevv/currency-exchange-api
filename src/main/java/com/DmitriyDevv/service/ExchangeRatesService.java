package com.DmitriyDevv.service;

import com.DmitriyDevv.dao.CurrenciesDataAccess;
import com.DmitriyDevv.dao.ExchangeRatesDataAccess;
import com.DmitriyDevv.dto.Currency;
import com.DmitriyDevv.dto.ExchangeRate;
import com.DmitriyDevv.exceptions.RequestException;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesService {

    private static final CurrenciesDataAccess currenciesDataAccess = new CurrenciesDataAccess();
    private static final ExchangeRatesDataAccess exchangeRatesDataAccess = new ExchangeRatesDataAccess();

    public static List<ExchangeRate> getExchangeRatesList() throws SQLException {
        return exchangeRatesDataAccess.getAll();
    }

    public static ExchangeRate getExchangeRate(String currencyPair) throws SQLException {
        if (currencyPair.length() != 6) {
            throw new RequestException("There are no currency pairs or they are incorrect",
                    HttpServletResponse.SC_BAD_REQUEST);
        }

        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3, 6);

        Currency baseCurrency = currenciesDataAccess.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currenciesDataAccess.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {


        }

        ExchangeRate exchangeRate = exchangeRatesDataAccess.getExchangeRateByCodes(baseCurrency, targetCurrency);

        if (exchangeRate == null) {
            throw new RequestException("The exchange rate for the pair was not found", HttpServletResponse.SC_NOT_FOUND);
        }

        return exchangeRate;
    }

}
