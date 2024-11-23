package com.sportshop.Service.Iml;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.RoleEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.RoleRepository;
import com.sportshop.Service.AccountService;

import com.sportshop.Service.MailService;
import com.sportshop.Utils.randomStringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


@Service
public class AccountServiceIml implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;



    @Override
    public AccountDTO findAccountByUserName(String email) {
        AccountEntity acc = accountRepository.findByemail(email);
        AccountDTO dto = AccountDTO.builder()
                .password(acc.getPassword())
                .role(RoleDTO.builder()
                        .name(acc.getRole().getName())
                        .build())
                        .build();
        return dto;
    }

    @Override
    public Result createAccount(AccountDTO accountDTO, HttpServletRequest request)
    {
        boolean checkexist = accountRepository.existsByemail(accountDTO.getEmail());
        if (checkexist)
        {
            return new Result(false,"Email đã được sử dụng");
        }
        AccountEntity accEntity = new AccountEntity();
        accEntity.setEmail(accountDTO.getEmail());
        String encodePassword = passwordEncoder.encode(accountDTO.getPassword());
        accEntity.setPassword(encodePassword);
        accEntity.setIs_disable("0");
        RoleEntity roleEntity = roleRepository.findByName("CUSTOMER");
        accEntity.setRole(roleEntity);
        try {
            accountRepository.save(accEntity);
            mailService.sendConfirmSignUp(accountDTO.getEmail(),request);
            return new Result(true,"Đăng ký tài khoản thành công");
        }
        catch (Exception e)
        {
            return new Result(false,"Thêm tài khoản thất bại");
        }

    }

    @Override
    public void confirmSignup(String email) {
        AccountEntity accEntity = accountRepository.findByemail(email);
        accEntity.setIs_disable("1");
        accountRepository.save(accEntity);
    }

    @Override
    public Result sendOTPToEmail(String email, HttpServletRequest request) {
        boolean checkexist = accountRepository.existsByemail(email);
        if (!checkexist)
        {
            return new Result(false,"Email chưa được đăng ký tài khoản");
        }

        String otpCode = randomStringUtil.randomOTP(6);
        AccountEntity accEntity = accountRepository.findByemail(email);
        accEntity.setOtp_code(otpCode);
        accEntity.setExpiry_date(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        accountRepository.save(accEntity);
        try {
            mailService.sendOTPtoResetPass(email,otpCode, request);
            return new Result(true,"Đã gửi mã OTP qua email");
        }
        catch (Exception e)
        {
            return new Result(false,"Gửi mã OTP thất bại");
        }
    }

    @Override
    public Result verifyOTPandSendPass(String otp, String email, HttpServletRequest request) {
        AccountEntity accEntity = accountRepository.findByemail(email);
        Date expyri_date = accEntity.getExpiry_date();
        Date currentTime = new Date();
        if (!Objects.equals(otp, accEntity.getOtp_code()))
        {
            return new Result(false,"Mã OTP không đúng!");
        } else if (currentTime.after(expyri_date)) {
            return new Result(false,"Mã OTP đã hết hạn!");
        }
        String newPassword = randomStringUtil.randomPassword(12);
        accEntity.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(accEntity);
        try {
            mailService.sendPassword(email,newPassword, request);
            return new Result(true,"Đã gửi mật khẩu qua email");
        }
        catch (Exception e)
        {
            return new Result(false,"Gửi mật khẩu thất bại");
        }
    }

    @Override
    public Result changePassword(String email, String oldPassword, String newPassword) {
        try {
            // Tìm tài khoản bằng email
            AccountEntity accEntity = accountRepository.findByemail(email);

            // Kiểm tra xem tài khoản có tồn tại không
            if (accEntity == null) {
                return new Result(false, "Tài khoản không tồn tại!");
            }

            // Kiểm tra mật khẩu cũ có đúng không
            if (!passwordEncoder.matches(oldPassword, accEntity.getPassword())) {
                System.out.println("Mật khẩu cũ không đúng!");
                return new Result(false, "Mật khẩu cũ không đúng!");
            }

            // Mã hóa mật khẩu mới và cập nhật
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            accEntity.setPassword(encodedNewPassword);

            // Lưu tài khoản
            accountRepository.save(accEntity);

            // Trả về kết quả thành công
            System.out.println("Đổi mật khẩu thành công");
            return new Result(true, "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về kết quả thất bại trong trường hợp xảy ra lỗi
            return new Result(false, "Đổi mật khẩu thất bại!");
        }
    }
}
