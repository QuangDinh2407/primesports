package com.sportshop.Service.Iml;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.UserInfoRepository;
//import com.sportshop.Repository.UserOrderRepository;
import com.sportshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import javax.smartcardio.Card;
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
                   .user_id(item.getUserInfo_id())
                   .name(item.getName())
                   .email(item.getEmail())
                   .phone(item.getPhone())
                   .birth(item.getBirth())
                   .created_at(item.getCreated_at())
                   .status(item.getStatus())
                   .gender(item.getGender())
                   .cart(new CartDTO(item.getCart().getCart_id()))
//                   .account(AccountDTO.builder()
//                           .email(item.getEmail())
//                           .password(item.getAccount().getPassword())
//                           .role(RoleDTO.builder()
//                                   .name(item.getAccount().getRole().getName())
//                                   .build())
//                           .build())
                   .build();

           userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Override
    public UserDTO findbyEmail(String email) {
        UserInfoEntity user = userInfoRepo.findByEmail(email);
        UserDTO userDTO = UserDTO.builder()
                .user_id(user.getUserInfo_id())
                .birth(user.getBirth())
                .address(user.getAddress())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .cart(new CartDTO(user.getCart().getCart_id()))
                .imagePath(user.getImage_path())
                .build();
        return userDTO;
    }

    @Override
    public Result updateInfoUser(UserDTO userDTO) {

        try{
            UserInfoEntity userInfoEntity = userInfoRepo.findByEmail(userDTO.getEmail());
            userInfoEntity.setName(userDTO.getName());
            userInfoEntity.setPhone(userDTO.getPhone());
            userInfoEntity.setAddress(userDTO.getAddress());
            userInfoEntity.setBirth(userDTO.getBirth());
            userInfoEntity.setGender(userDTO.getGender());
            userInfoRepo.save(userInfoEntity);
//            if (!Objects.equals(userDTO.getAccount().getPassword(), ""))
//            {
//                String passEncrypt = passwordEncoder.encode(userDTO.getAccount().getPassword());
//                AccountEntity accountEntity = accountRepository.findByemail(userDTO.getEmail());
//                accountEntity.setPassword(passEncrypt);
//                accountRepository.save(accountEntity);
//            }
            return new Result(true,"Thay đổi thông tin thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Thay đổi thông tin thất bại");
        }
    }
}
