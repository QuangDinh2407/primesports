package com.sportshop.Config;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {
            "/",                  // Home page
            "/auth/**",      // Sign-up page
            "/error/**",
            "/server-error",
            "/Assets/**"    // Static assets like CSS, JS, etc.
    };
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return username -> {
            // Find user by email (username) in the repository
            AccountEntity accountEntity = accountRepository.findByemail(username);
            if (accountEntity == null || Objects.equals(accountEntity.getIs_disable(), "0")) {
                throw new UsernameNotFoundException(username);
            }
            // Return a User object containing email, password, and roles
            return new org.springframework.security.core.userdetails.User(
                    accountEntity.getEmail(),
                    accountEntity.getPassword(),
                    List.of(new SimpleGrantedAuthority(accountEntity.getRole().getName()))
            );
        };
    }

    private String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24]; // 24 bytes = 192 bits
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RecaptchaAuthenticationFilter recaptchaAuthenticationFilter) throws Exception {
        //CSRF protection and configure request authorization
       http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()            // Permit requests to the whitelist URLs
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        //.requestMatchers("/employee/**").hasAuthority("EMPLOYEE")
                        .requestMatchers("/customer/**").hasAuthority("CUSTOMER")  // Only users with 'ADMIN' role can access admin routes
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied") // URL cho trang từ chối quyền truy cập
                )
                // Configure form-based login
               .addFilterBefore(recaptchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin((form) -> form
                        .loginPage("/auth/sign-in")                             // Custom login page URL
                        .loginProcessingUrl("/auth/sign-in")                    // URL to submit login credentials
                        .failureUrl("/auth/sign-in?error=true")                      // Redirect on login failure
//                        .defaultSuccessUrl("/success", true)                           // Redirect to home on successful login
                        .successHandler(new AuthSuccessHandler()) // Custom success handler for additional actions after login
                        .permitAll()                                            // Allow everyone to access the login page
                )
                .rememberMe((rememberMe) -> rememberMe
                        .key(generateRandomKey())
                        .tokenValiditySeconds(86400)
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("userAuth")
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/sign-out", "GET")) // Allow GET for logout                                           // URL for logging out
                        .logoutSuccessUrl("/auth/sign-in")                                          // Redirect to home page after logout
                        .deleteCookies("JSESSIONID", "userAuth")                // Delete cookies on logout
                        .invalidateHttpSession(true)                                               // Invalidate session on logout
                        .clearAuthentication(true)                                                  // Clear authentication on logout
                        .permitAll()                                                                // Allow everyone to log out
                );
        return http.build();
    }



}
