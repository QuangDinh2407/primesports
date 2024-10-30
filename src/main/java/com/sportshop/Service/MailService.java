package com.sportshop.Service;

import com.sportshop.Modal.Mail;
import com.sportshop.ModalDTO.AccountDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface MailService {

    void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException;

    void sendConfirmSignUp(String email,  HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;

    void sendOTPtoResetPass(String email, String otp, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;

    void sendPassword(String email, String newPassword, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
}
