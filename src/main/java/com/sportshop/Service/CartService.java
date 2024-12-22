package com.sportshop.Service;

import com.sportshop.ModalDTO.CartDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface CartService {

    String saveOrUpdateCart(CartDTO cartDTO);

    CartDTO moveCart(CartDTO userCart, CartDTO newCart);

    CartDTO addProductToCart(HttpSession session, String productId, Integer quantity, String size);

    CartDTO getAllItem (String email);
}
