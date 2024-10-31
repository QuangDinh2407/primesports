package com.sportshop.Service;

import com.sportshop.ModalDTO.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll ();
    UserDTO findbyEmail (String email);
}
