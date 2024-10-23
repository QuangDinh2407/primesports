package com.sportshop.Service;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;


public interface AccountService {
    AccountDTO findAccountByUserName(String username);

    Result createAccount(AccountDTO accountDTO);
}
