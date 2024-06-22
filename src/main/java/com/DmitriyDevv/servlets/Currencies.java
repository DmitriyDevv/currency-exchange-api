package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.DmitriyDevv.service.CurrenciesService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currencies")
public class Currencies extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            CurrenciesService.getCurrencyList(), HttpServletResponse.SC_OK));
        } catch (SQLException e) {
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            "Database error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        try {
            CurrenciesService.addCurrency(name, code, sign);
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            CurrenciesService.getCurrencyByCode(code),
                            HttpServletResponse.SC_CREATED));
        } catch (RequestException e) {
            ServletHelper.sendResponse(response, new ResponseData<>(e.getMessage(), e.getCode()));

        } catch (SQLException e) {
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            "Database error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
