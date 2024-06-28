package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeResponseDto(
        CurrencyDto baseCurrencyDto,
        CurrencyDto targetCurrencyDto,
        BigDecimal rate,
        double amount,
        BigDecimal convertedAmount) {}
