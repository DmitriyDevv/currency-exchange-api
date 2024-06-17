package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record Exchange(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate,
        double amount,
        BigDecimal convertedAmount) {}
