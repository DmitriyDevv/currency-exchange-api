package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeResponseDto(
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        BigDecimal rate,
        double amount,
        String convertedAmount) {}
