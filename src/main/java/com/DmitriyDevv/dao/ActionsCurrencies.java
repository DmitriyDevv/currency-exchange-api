package com.DmitriyDevv.dao;

import java.sql.SQLException;

public interface ActionsCurrencies<T> extends ActionsDB<T> {

    T getCurrencyByCode(String code) throws SQLException;

    void addCurrency(T currency) throws SQLException;
}
