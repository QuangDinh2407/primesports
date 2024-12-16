package com.sportshop.Converter;

import com.sportshop.Entity.CartDetailEntity;
import com.sportshop.Entity.CartEntity;
import com.sportshop.Entity.SizeEntity;
import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;
import com.sportshop.ModalDTO.ProductDTO;
import com.sportshop.Repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartDetailConverter {
    @Autowired
    ProductConverter productConverter;
    @Autowired
    private SizeRepository sizeRepository;

    public CartDetailDTO toDTO(CartDetailEntity cartDetailEntity){

        return CartDetailDTO.builder()
                .cartdetail_id(cartDetailEntity.getCartdetail_id())
                .amount(cartDetailEntity.getAmount())
                .size(cartDetailEntity.getSize().getName_size())
                .product(productConverter.toDTO(cartDetailEntity.getProduct()))
                .build();
    }

    public CartDetailEntity toEntity(CartDetailDTO cartDetailDTO, CartEntity cartEntity){

        SizeEntity sizeEntity = sizeRepository.findByName(cartDetailDTO.getSize());

        return CartDetailEntity.builder()
                .cartdetail_id(cartDetailDTO.getCartdetail_id())
                .amount(cartDetailDTO.getAmount())
                .size(sizeEntity)
                .product(productConverter.toEntity(cartDetailDTO.getProduct()))
                .cart(cartEntity)
                .build();
    }

}
