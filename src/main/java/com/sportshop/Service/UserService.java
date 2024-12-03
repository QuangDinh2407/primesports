package com.sportshop.Service;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll ();

    UserDTO findbyEmail (String email);

    Result updateInfoAdmin (UserDTO userDTO, MultipartFile file);

    Result updateInfoUser (UserDTO userDTO, MultipartFile file);
}
