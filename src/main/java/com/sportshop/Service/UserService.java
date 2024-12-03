package com.sportshop.Service;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.UserDTO;
import com.sportshop.ModalDTO.UserOrderDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll ();
    UserDTO findbyEmail (String email);
    Result updateInfoUser (UserDTO userDTO);
}
