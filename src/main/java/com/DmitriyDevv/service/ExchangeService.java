package com.DmitriyDevv.service;

import static com.DmitriyDevv.service.ServiceHelper.*;

import com.DmitriyDevv.dto.ExchangeRateDto;
import com.DmitriyDevv.dto.ExchangeResponseDto;
import com.DmitriyDevv.exceptions.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ExchangeService {
    public static ExchangeResponseDto getExchange(
            String baseCurrencyCode, String targetCurrencyCode, String amount) throws SQLException {

        String currencyPair = baseCurrencyCode + targetCurrencyCode;
        ExchangeRateDto exchangeRateDto = ExchangeRatesService.getExchangeRate(currencyPair);
        if (!isValidNumber(amount)) {
            throw new RequestException(
                    "Amount is not valid: must be a positive number",
                    HttpServletResponse.SC_BAD_REQUEST);
        }

        BigDecimal convertedAmount =
                exchangeRateDto
                        .rate()
                        .multiply(new BigDecimal(amount))
                        .setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResponseDto(
                exchangeRateDto.baseCurrencyDto(),
                exchangeRateDto.targetCurrencyDto(),
                exchangeRateDto.rate(),
                Double.parseDouble(amount),
                convertedAmount);
    }
}
