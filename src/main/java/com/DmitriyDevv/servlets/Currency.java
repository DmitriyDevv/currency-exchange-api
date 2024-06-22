package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.DmitriyDevv.service.CurrenciesService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

@WebServlet("/currency/*")
public class Currency extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String currencyCode = request.getPathInfo().replace("/", "");
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            CurrenciesService.getCurrencyByCode(currencyCode),
                            HttpServletResponse.SC_OK));
        } catch (RequestException | SQLException e) {
            ServletHelper.handleException(response, e);
        }
    }
}
