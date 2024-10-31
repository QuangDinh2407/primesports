package com.sportshop.Service.Iml;

import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.Repository.UserInfoRepository;
import com.sportshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceIml implements UserService {

    @Autowired
    UserInfoRepository userInfoRepo;

    @Override
    public List <UserDTO> findAll() {
        List<UserInfoEntity> items = userInfoRepo.findAll();
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();

        for (UserInfoEntity item : items) {
           UserDTO userDTO = UserDTO.builder()
                   .name(item.getName())
                   .email(item.getEmail())
                   .phone(item.getPhone())
                   .birth(item.getBirth())
                   .created_at(item.getCreated_at())
                   .status(item.getStatus())
                   .account(AccountDTO.builder()
                           .email(item.getEmail())
                           .password(item.getAccount().getPassword())
                           .role(RoleDTO.builder()
                                   .name(item.getAccount().getRole().getName())
                                   .build())
                           .build())
                   .build();

           userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Override
    public UserDTO findbyEmail(String email) {
        UserInfoEntity user = userInfoRepo.findByEmail(email);
        UserDTO userDTO = UserDTO.builder()
                .birth(user.getBirth())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .imagePath(user.getImage_path())
                .build();
        return userDTO;
    }
}
