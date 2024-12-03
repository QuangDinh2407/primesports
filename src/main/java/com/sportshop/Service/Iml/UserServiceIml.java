package com.sportshop.Service.Iml;

import com.cloudinary.Cloudinary;
import com.sportshop.Contants.StringContant;
import com.sportshop.Converter.CartConverter;
import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.UserInfoRepository;
import com.sportshop.Service.CloudinaryService;
import com.sportshop.Service.UserService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    ServletContext context;
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    CartConverter cartConverter;

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
                   .cart(cartConverter.toDTO(item.getCart()))
                   //                   .cart(new CartDTO(item.getCart().getCart_id()))
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
                .status(user.getStatus())
                .imagePath(user.getImage_path())
                .build();
        return userDTO;
    }

    @Override
    public Result updateInfoAdmin(UserDTO userDTO, MultipartFile file) {
        try{
            UserInfoEntity userInfoEntity = userInfoRepo.findByEmail(userDTO.getEmail());
            userInfoEntity.setName(userDTO.getName());
            userInfoEntity.setPhone(userDTO.getPhone());
            userInfoEntity.setAddress(userDTO.getAddress());
            userInfoEntity.setBirth(userDTO.getBirth());
            userInfoEntity.setGender(userDTO.getGender());
            if (!Objects.equals(userDTO.getAccount().getPassword(), ""))
            {
                String passEncrypt = passwordEncoder.encode(userDTO.getAccount().getPassword());
                AccountEntity accountEntity = accountRepository.findByemail(userDTO.getEmail());
                accountEntity.setPassword(passEncrypt);
                accountRepository.save(accountEntity);
            }
            if (!file.isEmpty())
            {
                try {
                    String imagePath = cloudinaryService.uploadFileToFolder(file, "admin");
                    userInfoEntity.setImage_path(imagePath);
                    userDTO.setImagePath(imagePath);
                } catch (IOException e) {

                    throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
                }
            }
            userInfoRepo.save(userInfoEntity);

            return new Result(true,"Thay đổi thông tin thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Thay đổi thông tin thất bại");
        }
    }

    @Override
    public Result updateInfoUser(UserDTO userDTO, MultipartFile file) {
        try{
            UserInfoEntity userInfoEntity = userInfoRepo.findByEmail(userDTO.getEmail());
            userInfoEntity.setName(userDTO.getName());
            userInfoEntity.setPhone(userDTO.getPhone());
            userInfoEntity.setAddress(userDTO.getAddress());
            userInfoEntity.setBirth(userDTO.getBirth());
            userInfoEntity.setGender(userDTO.getGender());
            if (!Objects.equals(userDTO.getAccount().getPassword(), ""))
            {
                String passEncrypt = passwordEncoder.encode(userDTO.getAccount().getPassword());
                AccountEntity accountEntity = accountRepository.findByemail(userDTO.getEmail());
                accountEntity.setPassword(passEncrypt);
                accountRepository.save(accountEntity);
            }
            if (!file.isEmpty())
            {
                try {
                    String imagePath = cloudinaryService.uploadFileToFolder(file, "customer");
                    userInfoEntity.setImage_path(imagePath);
                    userDTO.setImagePath(imagePath);
                } catch (IOException e) {

                    throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
                }
            }
            userInfoRepo.save(userInfoEntity);

            return new Result(true,"Thay đổi thông tin thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Thay đổi thông tin thất bại");
        }
    }
}
