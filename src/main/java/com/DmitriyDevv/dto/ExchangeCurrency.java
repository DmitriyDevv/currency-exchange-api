package com.DmitriyDevv.dto;

import java.math.BigDecimal;

public record ExchangeCurrency(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertAmount) {

}
