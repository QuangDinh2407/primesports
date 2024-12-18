package com.sportshop.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
       // boolean isEmployee = authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
        boolean isCustomer = authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"));

        String email = authentication.getName();
        // Lưu email vào session
        HttpSession session = request.getSession();
        session.setAttribute("email", email);

//        if (isAdmin)
//        {
//            response.sendRedirect("/admin");
//        } else if (isEmployee) {
//            response.sendRedirect("/employee");
//        }
//        else {
//            response.sendRedirect("/customer");
//        }

        if (isAdmin)
        {
            response.sendRedirect("/admin");
        }
        else {
            response.sendRedirect("/customer");
        }

    }
}
