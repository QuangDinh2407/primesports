package com.sportshop.Config;

import com.sportshop.Service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = request.getParameter("username"); // Lấy username từ form login
        if (username != null && request.getRequestURI().contains("/auth/sign-in")) {
            if (rateLimitService.isBlocked(username)) {
                response.setStatus(429); // HTTP 429 Too Many Requests
//                response.sendRedirect("/auth/sign-in?error=rate_limit");
                response.getWriter().write("Too many login attempts. Please try again later.");
                return;
            }
            rateLimitService.recordAttempt(username);
        }

        filterChain.doFilter(request, response);
    }
}
