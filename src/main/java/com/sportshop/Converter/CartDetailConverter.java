package com.sportshop.Converter;

import com.sportshop.Entity.CartDetailEntity;
import com.sportshop.Entity.CartEntity;
import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartDetailConverter {
    @Autowired
    ProductConverter productConverter;
    public CartDetailDTO toDTO(CartDetailEntity cartDetailEntity){

        return CartDetailDTO.builder()
                .cartdetail_id(cartDetailEntity.getCartdetail_id())
                .amount(cartDetailEntity.getAmount())
                .product(productConverter.toDTO(cartDetailEntity.getProduct()))
                .build();
    }

    public CartDetailEntity toEntity(CartDetailDTO cartDetailDTO, CartEntity cartEntity){

        return CartDetailEntity.builder()
                .cartdetail_id(cartDetailDTO.getCartdetail_id())
                .amount(cartDetailDTO.getAmount())
                .product(productConverter.toEntity(cartDetailDTO.getProduct()))
                .cart(cartEntity)
                .build();
    }

}
