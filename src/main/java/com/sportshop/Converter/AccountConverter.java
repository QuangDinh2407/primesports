package com.sportshop.Converter;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.CartEntity;
import com.sportshop.Entity.RoleEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Repository.RoleRepository;
import com.sportshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AccountConverter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    public AccountDTO toDTO(AccountEntity accountEntity) {
        UserDTO userDTO = userService.findbyEmail(accountEntity.getEmail());
        return AccountDTO.builder()
                .email(accountEntity.getEmail())
                .password(accountEntity.getPassword())
                .is_disable(accountEntity.getIs_disable())
                .otp_code(accountEntity.getOtp_code())
                .expiry_date(accountEntity.getExpiry_date())
                .role(RoleDTO.builder()
                        .name(accountEntity.getRole().getName())
                        .build())
                .userInfo(userDTO)
                .build();
    }

    public AccountEntity toEntity(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }
        String encodePassword = passwordEncoder.encode(accountDTO.getPassword());
        RoleEntity roleEntity = roleRepository.findByName("CUSTOMER");
        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .email(accountDTO.getEmail())
                .name(accountDTO.getUserInfo().getName())
                .phone(accountDTO.getUserInfo().getPhone())
                .address(accountDTO.getUserInfo().getAddress())
                .birth(accountDTO.getUserInfo().getBirth())
                .status(accountDTO.getUserInfo().getStatus())
                .gender(accountDTO.getUserInfo().getGender())
                .cart(new CartEntity())
                .created_at(new Date())
                .build();

        AccountEntity accountEntity = AccountEntity.builder()
                .email(accountDTO.getEmail())
                .password(encodePassword)
                .is_disable(accountDTO.getIs_disable())
                .role(roleEntity)
                .build();

        accountEntity.setUser(userInfoEntity);

        return accountEntity;
    }

}

