package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeRate(int ID, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {

}
