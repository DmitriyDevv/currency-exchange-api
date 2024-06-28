package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(
        int id, CurrencyDto baseCurrencyDto, CurrencyDto targetCurrencyDto, BigDecimal rate) {}
