package com.DmitriyDevv.service;

import com.DmitriyDevv.dto.Exchange;
import com.DmitriyDevv.dto.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ExchangeService {
    public static Exchange getExchange(
            String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException {

        String currencyPair = baseCurrencyCode + targetCurrencyCode;

        ExchangeRate exchangeRate = ExchangeRatesService.getExchangeRate(currencyPair);

        BigDecimal convertedAmount = exchangeRate.rate().multiply(new BigDecimal(amount));

        return new Exchange(
                exchangeRate.baseCurrency(),
                exchangeRate.targetCurrency(),
                exchangeRate.rate(),
                amount,
                convertedAmount);
    }
}
