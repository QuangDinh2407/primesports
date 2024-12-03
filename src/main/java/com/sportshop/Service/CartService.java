package com.sportshop.Service;

import com.sportshop.ModalDTO.CartDTO;

public interface CartService {
    public String saveOrUpdateCart(CartDTO cartDTO);

    String deleteCart(CartDTO cartDTO);
}
