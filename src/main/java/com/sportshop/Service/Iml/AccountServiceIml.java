package com.sportshop.Service.Iml;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceIml implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountDTO findAccountByUserName(String username) {
        AccountEntity acc = accountRepository.findByuserName(username);
        AccountDTO dto = AccountDTO.builder()
                .password(acc.getPassword())
                .role(RoleDTO.builder()
                        .name(acc.getRole().getName())
                        .build())
                        .build();
        return dto;
    }
}
