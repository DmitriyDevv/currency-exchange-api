package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.DmitriyDevv.service.ExchangeRatesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRate extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        if (method.equalsIgnoreCase("PATCH")) {
            doPost(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String currencyPair = getCurrencyPair(request);
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            ExchangeRatesService.getExchangeRate(currencyPair),
                            HttpServletResponse.SC_OK));
        } catch (RequestException | SQLException e) {
            ServletHelper.handleException(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String currencyPair = getCurrencyPair(request);
        String newRate = request.getParameter("rate");

        try {
            ExchangeRatesService.updateRate(currencyPair, newRate);
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            ExchangeRatesService.getExchangeRate(currencyPair),
                            HttpServletResponse.SC_OK));
        } catch (RequestException | SQLException e) {
            ServletHelper.handleException(response, e);
        }
    }

    private String getCurrencyPair(HttpServletRequest request) {
        return request.getPathInfo().split("/")[1].toUpperCase();
    }
}
