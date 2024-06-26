package com.DmitriyDevv.service;

import static com.DmitriyDevv.service.ServiceHelper.*;

import com.DmitriyDevv.dto.Exchange;
import com.DmitriyDevv.dto.ExchangeRate;
import com.DmitriyDevv.exceptions.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ExchangeService {
    public static Exchange getExchange(
            String baseCurrencyCode, String targetCurrencyCode, String amount) throws SQLException {

        String currencyPair = baseCurrencyCode + targetCurrencyCode;
        ExchangeRate exchangeRate = ExchangeRatesService.getExchangeRate(currencyPair);
        if (!isValidNumber(amount)) {
            throw new RequestException(
                    "Amount is not valid: must be a positive number",
                    HttpServletResponse.SC_BAD_REQUEST);
        }

        BigDecimal convertedAmount = exchangeRate.rate().multiply(new BigDecimal(amount));

        return new Exchange(
                exchangeRate.baseCurrency(),
                exchangeRate.targetCurrency(),
                exchangeRate.rate(),
                Double.parseDouble(amount),
                convertedAmount);
    }
}
