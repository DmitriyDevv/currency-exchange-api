package com.DmitriyDevv.service;

public class ServiceHelper {
    public static boolean isValidCurrencyPair(String currencyPair) {
        int pairLength = 6;
        return currencyPair != null && currencyPair.trim().length() == pairLength;
    }

    public static boolean isValidCurrencyCode(String currencyCode) {
        int codeLength = 3;
        return currencyCode != null && currencyCode.trim().length() == codeLength;
    }

    public static boolean isValidCurrencyName(String currencyName) {
        return currencyName != null && !currencyName.trim().isEmpty();
    }

    public static boolean isValidCurrencySing(String currencySing) {
        int singLength = 1;
        return currencySing != null && currencySing.trim().length() == singLength;
    }

    public static boolean isValidNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }

        if (number.matches("^\\d+(\\.\\d+)?$")) {
            try {
                double num = Double.parseDouble(number);
                return num > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }
}
