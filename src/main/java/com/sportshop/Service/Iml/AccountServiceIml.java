package com.sportshop.Service.Iml;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.RoleEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.RoleRepository;
import com.sportshop.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountServiceIml implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public Result createAccount(AccountDTO accountDTO)
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
        RoleEntity roleEntity = roleRepository.findByName("CUSTOMER");
        accEntity.setRole(roleEntity);
        try {
            accountRepository.save(accEntity);
            return new Result(true,"Đăng ký tài khoản thành công");
        }
        catch (Exception e)
        {
            return new Result(false,"Thêm tài khoản thất bại");
        }

    }

}
