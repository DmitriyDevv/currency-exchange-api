package com.DmitriyDevv.dao;

import com.DmitriyDevv.dto.Currency;

import java.sql.SQLException;

public interface ActionsExchangeRates<T> extends ActionsDB<T> {

    T getExchangeRateByCodes(Currency baseCurrency, Currency targetCurrency) throws SQLException;

    void addExchangeRate(T exchangeRate);
}
