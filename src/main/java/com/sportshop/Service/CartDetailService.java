package com.sportshop.Service;

import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import jakarta.servlet.http.HttpSession;

public interface CartDetailService {

    String saveOrUpdateCartDetail(CartDetailDTO cartDetailDTO);

    String deleteInfoCartDetail(CartDetailDTO cartDetailDTO);


}
