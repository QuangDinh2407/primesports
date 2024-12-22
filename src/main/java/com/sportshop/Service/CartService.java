package com.sportshop.Service;

import com.sportshop.ModalDTO.CartDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CartService {

    String saveOrUpdateCart(CartDTO cartDTO);

    CartDTO moveCart(CartDTO userCart, CartDTO newCart);

    CartDTO addProductToCart(HttpSession session, String productId, Integer quantity, String size);

    CartDTO getAllItem (String email);

    CartDTO findCart(String cart_id);

    void deleteItems (CartDTO cartDTO, List<String> productId,HttpSession session);
}
