package com.sportshop.Converter;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConverter {

    @Autowired
    UserService userService;


}
