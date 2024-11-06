package com.sportshop.Service.Iml;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.UserInfoRepository;
import com.sportshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceIml implements UserService {

    @Autowired
    UserInfoRepository userInfoRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

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
                .address(user.getAddress())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .imagePath(user.getImage_path())
                .build();
        return userDTO;
    }

    @Override
    public Result updateInfoEmployee(UserDTO userDTO) {

        try{
            UserInfoEntity userInfoEntity = userInfoRepo.findByEmail(userDTO.getEmail());
            userInfoEntity.setName(userDTO.getName());
            userInfoEntity.setPhone(userDTO.getPhone());
            userInfoEntity.setAddress(userDTO.getAddress());
            userInfoEntity.setBirth(userDTO.getBirth());
            userInfoRepo.save(userInfoEntity);
            if (!Objects.equals(userDTO.getAccount().getPassword(), ""))
            {
                String passEncrypt = passwordEncoder.encode(userDTO.getAccount().getPassword());
                AccountEntity accountEntity = accountRepository.findByemail(userDTO.getEmail());
                accountEntity.setPassword(passEncrypt);
                accountRepository.save(accountEntity);

            }
            return new Result(true,"Thay đổi thông tin thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Thay đổi thông tin thất bại");
        }
    }
}
