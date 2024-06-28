package com.DmitriyDevv.service;

import static com.DmitriyDevv.service.ServiceHelper.*;

import com.DmitriyDevv.dao.CurrenciesDataAccess;
import com.DmitriyDevv.dto.CurrencyDto;
import com.DmitriyDevv.exceptions.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import org.sqlite.SQLiteErrorCode;

import java.sql.SQLException;
import java.util.List;

public class CurrenciesService {

    private static final CurrenciesDataAccess currencies = new CurrenciesDataAccess();

    public static List<CurrencyDto> getCurrencyList() throws SQLException {
        return currencies.getAll();
    }

    public static CurrencyDto getCurrencyByCode(String code) throws SQLException {
        if (!isValidCurrencyCode(code)) {
            throw new RequestException(
                    "The currency code is missing", HttpServletResponse.SC_BAD_REQUEST);
        }

        CurrencyDto currencyDto = currencies.getCurrencyByCode(code);

        if (currencyDto == null) {
            throw new RequestException(
                    "The currency was not found", HttpServletResponse.SC_NOT_FOUND);
        }

        return currencyDto;
    }

    public static void addCurrency(String name, String code, String sign) throws SQLException {
        if (!isValidCurrencyName(name)
                || !isValidCurrencyCode(code)
                || !isValidCurrencySing(sign)) {
            throw new RequestException(
                    "The required form field is missing", HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            CurrencyDto newCurrencyDto = new CurrencyDto(0, name, code, sign);
            currencies.addCurrency(newCurrencyDto);
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
