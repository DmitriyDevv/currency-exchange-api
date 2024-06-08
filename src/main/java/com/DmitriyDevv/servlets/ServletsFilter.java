package com.DmitriyDevv.servlets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter({"/currencies", "/exchangeRate/*", "/currency/*", "/exchangeRates", "/exchange"})
public class ServletsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
