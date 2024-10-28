package com.sportshop.Service.Iml;

import com.sportshop.Entity.OTPEntity;
import com.sportshop.Modal.Result;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Service.MailService;
import com.sportshop.Service.OTPService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class OTPServiceIml implements OTPService {

    private static final int OTP_LENGTH = 6;

    private static final long EXPIRY_TIME = 5 * 60 * 1000; // 5 phút

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MailService mailService;

    @Override
    public Result generateRandomOTP(String email, HttpServletRequest request) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        boolean checkexist = accountRepository.existsByemail(email);
        if (!checkexist)
        {
            return new Result(false,"Email chưa được đăng ký tài khoản");
        }
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setAccount(accountRepository.findByemail(email));
        otpEntity.setCode(otp.toString());
        otpEntity.setExpiry_date(new Date(System.currentTimeMillis() + EXPIRY_TIME));
        try {
            mailService.sendOTPtoResetPass(email,otp.toString(), request);
            return new Result(true,"Đã gửi mã OTP qua email");
        }
        catch (Exception e)
        {
            return new Result(false,"Gửi mã OTP thất bại");
        }
    }


}
