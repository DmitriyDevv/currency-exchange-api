package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeRate(
        int id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {}
