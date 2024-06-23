package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.DmitriyDevv.service.ExchangeRatesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRate extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        if (method.equalsIgnoreCase("PATCH")) {
            doPatch(request, response);
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

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) {
        String currencyPair = getCurrencyPair(request);
        String newRate = getParameters(request).get("rate");

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

    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] pairs = sb.toString().split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }

        return parameters;
    }
}
