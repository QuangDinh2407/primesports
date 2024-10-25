package com.sportshop.Service;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import jakarta.servlet.http.HttpServletRequest;


public interface AccountService {
    AccountDTO findAccountByUserName(String username);

    Result createAccount(AccountDTO accountDTO, HttpServletRequest request);

    void confirmSignup(String email);
}
