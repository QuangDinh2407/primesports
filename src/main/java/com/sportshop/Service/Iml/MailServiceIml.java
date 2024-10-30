package com.sportshop.Service.Iml;

import com.sportshop.Modal.Mail;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.Service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class MailServiceIml implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("PRIMESPORTS")
    private String personal;

    @Override
    public void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from, personal);
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getBody(), true);
        mailSender.send(message);
    }

    @Override
    public void sendConfirmSignUp(String email, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, personal);
        helper.setTo(email);
        helper.setSubject("Xác nhận đăng ký tài khoản");

        // Thiết lập ngữ cảnh cho Thymeleaf
        Context context = new Context();
        context.setVariable("email", email);
        String confirmationLink = "http://" + request.getServerName() + ":" + request.getServerPort() + "/auth/confirm-signup";
        context.setVariable("confirmationLink", confirmationLink);

        // Tạo nội dung HTML từ template
        String htmlContent = templateEngine.process("Auth/confirm-signup", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Override
    public void sendOTPtoResetPass(String email,String otp, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, personal);
        helper.setTo(email);
        helper.setSubject("Xác nhận khôi phục mật khẩu");

        // Thiết lập ngữ cảnh cho Thymeleaf
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("otpCode", otp);

        // Tạo nội dung HTML từ template
        String htmlContent = templateEngine.process("Auth/send-otp", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Override
    public void sendPassword(String email, String newPassword, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, personal);
        helper.setTo(email);
        helper.setSubject("Khôi phục mật khẩu");

        // Thiết lập ngữ cảnh cho Thymeleaf
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("newPassword", newPassword);

        // Tạo nội dung HTML từ template
        String htmlContent = templateEngine.process("Auth/send-password", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }


}
