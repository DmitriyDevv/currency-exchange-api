package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(
        int id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate) {}
