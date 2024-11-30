package com.sportshop.Config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated())
        {
            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
            //boolean isEmployee = authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));
            boolean isCustomer = authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"));

//            if(isAdmin) {
//                return "admin";
//            }
//            else if (isEmployee) {
//                return "employee";
//            }
//            else if (isCustomer) {
//                return "customer";
//            }

            if(isAdmin) {
                return "admin";
            }
            else if(isCustomer){
                return "customer";
            }
            else {
                return "auth";
            }
        }
        return "auth";
    }
}
