package com.sportshop.Config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RecaptchaAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "6LejbI8qAAAAAK1Y2NQxb9EnwfX7Fa50XWzPKj38";
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Chỉ kiểm tra reCAPTCHA khi người dùng đăng nhập
        if ("/auth/sign-in".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
                response.sendRedirect("/auth/sign-in?error=missingRecapcha");
                return;
            }
            // Gửi request xác minh đến Google
            boolean isValid = verifyRecaptcha(recaptchaResponse);
            if (!isValid) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid reCAPTCHA");
                return;
            }
        }
        // Tiếp tục chuỗi filter
        filterChain.doFilter(request, response);
    }

    private boolean verifyRecaptcha(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        // Chuyển đổi Map thành chuỗi "key=value&key2=value2"
        String body = "secret=" + SECRET_KEY + "&response=" + recaptchaResponse;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                RECAPTCHA_VERIFY_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        return responseBody != null && Boolean.TRUE.equals(responseBody.get("success"));
    }

}