package com.sportshop.Service;

import com.sportshop.ModalDTO.CartDTO;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    public String saveOrUpdateCart(CartDTO cartDTO);

    CartDTO addProductToCart(HttpSession session, String productId, Integer quantity);
}
