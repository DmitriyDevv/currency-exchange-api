package com.DmitriyDevv.servlets;

import com.DmitriyDevv.dto.ResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ServletHelper {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static <T> String getJson(T dtoObject) {
        return gson.toJson(dtoObject);
    }

    public static <T> void sendResponse(HttpServletResponse response, ResponseData<T> responseData) {
        try {
            response.getWriter().println(getJson(responseData.payload()));
            response.setStatus(responseData.responseCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
