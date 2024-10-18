package com.sportshop.Service;

import com.sportshop.ModalDTO.AccountDTO;

public interface AccountService {
    AccountDTO findAccountByUserName(String username);
}
