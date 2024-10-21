package com.sportshop.Service;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.ModalDTO.AccountDTO;


public interface AccountService {
    AccountDTO findAccountByUserName(String username);

    AccountEntity createAccount(AccountDTO accountDTO);
}
