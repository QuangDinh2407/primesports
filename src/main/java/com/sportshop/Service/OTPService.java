package com.sportshop.Service;

import com.sportshop.Entity.OTPEntity;
import com.sportshop.Modal.Result;
import jakarta.servlet.http.HttpServletRequest;

public interface OTPService {

    Result generateRandomOTP(String email, HttpServletRequest request);
}
