package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.service.ExchangeRatesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRates extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletHelper.sendResponse(response, new ResponseData<>(ExchangeRatesService.getExchangeRatesList(),
                    HttpServletResponse.SC_OK));
        } catch (SQLException e) {
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>("Database error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    }
}