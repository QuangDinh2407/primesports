package com.sportshop.Config;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.AccountService;
import com.sportshop.Utils.randomStringUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Lazy
    @Autowired
    AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
       // boolean isEmployee = authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
        boolean isCustomer = authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"));
        // Kiểm tra nếu authentication là OAuth2User (Google login) hoặc UserDetails (login thông thường)
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            Object  pictureObj = oauth2User.getAttribute("picture");
            String picture = null;
            if (pictureObj instanceof LinkedHashMap) {
                LinkedHashMap<?, ?> pictureMap = (LinkedHashMap<?, ?>) pictureObj;
                LinkedHashMap<?, ?> dataMap = (LinkedHashMap<?, ?>) pictureMap.get("data");
                picture = (String) dataMap.get("url");
            }
            else {
                picture = oauth2User.getAttribute("picture");
            }
            String passsword = randomStringUtil.randomPassword(12);
            AccountDTO account = AccountDTO.builder()
                    .email(oauth2User.getAttribute("email"))
                    .password(passsword)
                    .userInfo(UserDTO.builder()
                            .name(oauth2User.getAttribute("name"))
                            .imagePath(picture)
                            .build())
                    .build();
            AccountEntity accountEntity = accountService.processOAuth2Account(account);

            String roleName = accountEntity.getRole().getName();
            Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>();
            updatedAuthorities.add(new SimpleGrantedAuthority(roleName));

            // Cập nhật SecurityContext với quyền mới
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    updatedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
        }
        else {
            String email = authentication.getName();
            System.out.println(email);
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
        }
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

            response.sendRedirect("/");
        }

    }
}
