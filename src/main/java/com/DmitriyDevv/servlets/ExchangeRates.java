package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.DmitriyDevv.service.ExchangeRatesService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRates extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            ExchangeRatesService.getExchangeRatesList(),
                            HttpServletResponse.SC_OK));
        } catch (RequestException | SQLException e) {
            ServletHelper.handleException(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        try {
            ExchangeRatesService.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            ExchangeRatesService.getExchangeRate(
                                    baseCurrencyCode + targetCurrencyCode),
                            HttpServletResponse.SC_OK));
        } catch (RequestException | SQLException e) {
            ServletHelper.handleException(response, e);
        }
    }
}
