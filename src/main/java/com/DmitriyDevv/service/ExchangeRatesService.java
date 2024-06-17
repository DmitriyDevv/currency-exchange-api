package com.DmitriyDevv.service;

import com.DmitriyDevv.dao.CurrenciesDataAccess;
import com.DmitriyDevv.dao.ExchangeRatesDataAccess;
import com.DmitriyDevv.dto.Currency;
import com.DmitriyDevv.dto.ExchangeRate;
import com.DmitriyDevv.exceptions.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesService {

    private static final CurrenciesDataAccess currenciesDataAccess = new CurrenciesDataAccess();
    private static final ExchangeRatesDataAccess exchangeRatesDataAccess =
            new ExchangeRatesDataAccess();

    public static List<ExchangeRate> getExchangeRatesList() throws SQLException {
        return exchangeRatesDataAccess.getAll();
    }

    public static ExchangeRate getExchangeRate(String currencyPair) throws SQLException {
        if (currencyPair.length() != 6) {
            throw new RequestException(
                    "Incorrect entry of currency pairs", HttpServletResponse.SC_BAD_REQUEST);
        }

        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3, 6);

        Currency baseCurrency = currenciesDataAccess.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currenciesDataAccess.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new RequestException(
                    "The exchange rate for the pair was not found",
                    HttpServletResponse.SC_NOT_FOUND);
        }

        ExchangeRate directExchangeRate =
                exchangeRatesDataAccess.getExchangeRateForPair(baseCurrency, targetCurrency);

        if (directExchangeRate != null) {
            return directExchangeRate;
        }

        ExchangeRate reverseExchangeRate = getReverseExchangeRate(targetCurrency, baseCurrency);

        if (reverseExchangeRate != null) {
            return reverseExchangeRate;
        }

        ExchangeRate crossExchangeRate = getCrossExchangeRate(targetCurrency, baseCurrency);

        if (crossExchangeRate != null) {
            return crossExchangeRate;
        } else {
            throw new RequestException(
                    "The exchange rate for the pair was not found",
                    HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private static ExchangeRate getReverseExchangeRate(
            Currency targetCurrency, Currency baseCurrency) throws SQLException {
        ExchangeRate reverseExchangeRate =
                exchangeRatesDataAccess.getExchangeRateForPair(targetCurrency, baseCurrency);

        if (reverseExchangeRate != null) {
            BigDecimal reverseRate = calculateReverseCourse(reverseExchangeRate.rate());

            return new ExchangeRate(
                    reverseExchangeRate.id(), baseCurrency, targetCurrency, reverseRate);
        }

        return null;
    }

    private static ExchangeRate getCrossExchangeRate(Currency baseCurrency, Currency targetCurrency)
            throws SQLException {

        Currency usd = currenciesDataAccess.getCurrencyByCode("USD");

        if (usd != null) {
            ExchangeRate usdBaseCurrency =
                    exchangeRatesDataAccess.getExchangeRateForPair(usd, baseCurrency);
            ExchangeRate usdTargetCurrency =
                    exchangeRatesDataAccess.getExchangeRateForPair(usd, targetCurrency);

            if (usdBaseCurrency != null && usdTargetCurrency != null) {
                BigDecimal crossRate =
                        calculateReverseCourse(usdBaseCurrency.rate())
                                .multiply(usdTargetCurrency.rate());

                return new ExchangeRate(200, baseCurrency, targetCurrency, crossRate);
            }
        }
        return null;
    }

    private static BigDecimal calculateReverseCourse(BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, MathContext.DECIMAL128);
    }
}
