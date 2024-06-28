package com.DmitriyDevv.service;

import static com.DmitriyDevv.service.ServiceHelper.*;

import com.DmitriyDevv.dao.CurrenciesDataAccess;
import com.DmitriyDevv.dao.ExchangeRatesDataAccess;
import com.DmitriyDevv.dto.CurrencyDto;
import com.DmitriyDevv.dto.ExchangeRateDto;
import com.DmitriyDevv.exceptions.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import org.sqlite.SQLiteErrorCode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesService {

    private static final CurrenciesDataAccess currenciesDataAccess = new CurrenciesDataAccess();
    private static final ExchangeRatesDataAccess exchangeRatesDataAccess =
            new ExchangeRatesDataAccess();

    public static List<ExchangeRateDto> getExchangeRatesList() throws SQLException {
        return exchangeRatesDataAccess.getAll();
    }

    public static ExchangeRateDto getExchangeRate(String currencyPair) throws SQLException {
        if (!isValidCurrencyPair(currencyPair)) {
            throw new RequestException(
                    "Incorrect entry of currency pairs", HttpServletResponse.SC_BAD_REQUEST);
        }

        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3, 6);

        CurrencyDto baseCurrencyDto = currenciesDataAccess.getCurrencyByCode(baseCurrencyCode);
        CurrencyDto targetCurrencyDto = currenciesDataAccess.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrencyDto == null || targetCurrencyDto == null) {
            throw new RequestException(
                    "The exchange rate for the pair was not found",
                    HttpServletResponse.SC_NOT_FOUND);
        }

        ExchangeRateDto directExchangeRateDto =
                exchangeRatesDataAccess.getExchangeRateForPair(baseCurrencyDto, targetCurrencyDto);

        if (directExchangeRateDto != null) {
            return directExchangeRateDto;
        }

        ExchangeRateDto reverseExchangeRateDto =
                getReverseExchangeRate(targetCurrencyDto, baseCurrencyDto);

        if (reverseExchangeRateDto != null) {
            return reverseExchangeRateDto;
        }

        ExchangeRateDto crossExchangeRateDto =
                getCrossExchangeRate(targetCurrencyDto, baseCurrencyDto);

        if (crossExchangeRateDto != null) {
            return crossExchangeRateDto;
        } else {
            throw new RequestException(
                    "The exchange rate for the pair was not found",
                    HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public static void addExchangeRate(
            String baseCurrencyCode, String targetCurrencyCode, String rate) throws SQLException {

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new RequestException(
                    "Error: currency codes match", HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!isValidNumber(rate)) {
            throw new RequestException(
                    "Rate is not valid: must be a positive number",
                    HttpServletResponse.SC_BAD_REQUEST);
        }

        CurrencyDto baseCurrencyDto = CurrenciesService.getCurrencyByCode(baseCurrencyCode);
        CurrencyDto targetCurrencyDto = CurrenciesService.getCurrencyByCode(targetCurrencyCode);

        try {
            exchangeRatesDataAccess.addExchangeRate(
                    baseCurrencyDto.Id(), targetCurrencyDto.Id(), new BigDecimal(rate));
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new RequestException(
                        "A currency pair with this code already exists",
                        HttpServletResponse.SC_CONFLICT);
            } else {
                throw new SQLException();
            }
        }
    }

    public static void updateRate(String currencyPair, String newRate) throws SQLException {
        ExchangeRateDto exchangeRateDto = getExchangeRate(currencyPair);

        if (newRate == null || newRate.isEmpty()) {
            throw new RequestException(
                    "One of the fields is missing or incorrect",
                    HttpServletResponse.SC_BAD_REQUEST);
        }

        exchangeRatesDataAccess.updateRate(
                exchangeRateDto.baseCurrency().Id(),
                exchangeRateDto.targetCurrency().Id(),
                new BigDecimal(newRate));
    }

    private static ExchangeRateDto getReverseExchangeRate(
            CurrencyDto targetCurrencyDto, CurrencyDto baseCurrencyDto) throws SQLException {
        ExchangeRateDto reverseExchangeRateDto =
                exchangeRatesDataAccess.getExchangeRateForPair(targetCurrencyDto, baseCurrencyDto);

        if (reverseExchangeRateDto != null) {
            BigDecimal reverseRate = calculateReverseCourse(reverseExchangeRateDto.rate());

            return new ExchangeRateDto(
                    reverseExchangeRateDto.id(), baseCurrencyDto, targetCurrencyDto, reverseRate);
        }

        return null;
    }

    private static ExchangeRateDto getCrossExchangeRate(
            CurrencyDto baseCurrencyDto, CurrencyDto targetCurrencyDto) throws SQLException {

        CurrencyDto usd = currenciesDataAccess.getCurrencyByCode("USD");

        if (usd != null) {
            ExchangeRateDto usdBaseCurrency =
                    exchangeRatesDataAccess.getExchangeRateForPair(usd, baseCurrencyDto);
            ExchangeRateDto usdTargetCurrency =
                    exchangeRatesDataAccess.getExchangeRateForPair(usd, targetCurrencyDto);

            if (usdBaseCurrency != null && usdTargetCurrency != null) {
                BigDecimal crossRate =
                        calculateReverseCourse(usdBaseCurrency.rate())
                                .multiply(usdTargetCurrency.rate());

                return new ExchangeRateDto(200, baseCurrencyDto, targetCurrencyDto, crossRate);
            }
        }
        return null;
    }

    private static BigDecimal calculateReverseCourse(BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, MathContext.DECIMAL128);
    }
}
