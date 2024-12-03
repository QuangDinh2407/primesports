package com.sportshop.Service;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AccountService {
    AccountDTO findAccountByUserName(String username);

    Result createAccount(AccountDTO accountDTO, HttpServletRequest request);

    void confirmSignup(String email);

    Result sendOTPToEmail(String email, HttpServletRequest request);

    Result verifyOTPandSendPass(String otp, String email, HttpServletRequest request);

    List <AccountDTO> getAll ();

    Page<AccountDTO> getAllCustomer(Pageable pageable, String search, String status);

    Result deleteByEmail(String email);

    Result updateAccountCustomer (AccountDTO accountDTO, MultipartFile file);

    Result addAccountCustomer (AccountDTO accountDTO, MultipartFile file);

    AccountEntity processOAuth2Account(AccountDTO accountDTO);

    Result changePassword(String email, String oldPassword, String newPassword);

}
