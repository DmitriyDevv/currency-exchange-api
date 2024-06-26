package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.DmitriyDevv.service.ExchangeService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

@WebServlet("/exchange")
public class Exchange extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            String amount = request.getParameter("amount");
            ServletHelper.sendResponse(
                    response,
                    new ResponseData<>(
                            ExchangeService.getExchange(from, to, amount),
                            HttpServletResponse.SC_OK));

        } catch (RequestException | SQLException e) {
            ServletHelper.handleException(response, e);
        }
    }
}
