package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ErrorMessage;
import com.DmitriyDevv.dto.ResponseData;
import com.DmitriyDevv.exceptions.RequestException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class ServletHelper {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static <T> String getJson(T dtoObject) {
        return gson.toJson(dtoObject);
    }

    public static void handleException(HttpServletResponse response, Exception e) {
        if (e instanceof RequestException ex) {
            sendResponse(
                    response, new ResponseData<>(new ErrorMessage(ex.getMessage()), ex.getCode()));
        } else if (e instanceof SQLException) {
            sendResponse(
                    response,
                    new ResponseData<>(
                            new ErrorMessage("Database error"),
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }

    public static <T> void sendResponse(
            HttpServletResponse response, ResponseData<T> responseData) {
        try {
            response.getWriter().println(getJson(responseData.payload()));
            response.setStatus(responseData.responseCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
