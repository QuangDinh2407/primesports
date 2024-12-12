package com.sportshop.Converter;

import com.sportshop.Entity.CartEntity;
import com.sportshop.Entity.CartDetailEntity;
import com.sportshop.ModalDTO.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class CartConverter {
    @Autowired
    CartDetailConverter cartDetailConverter;

//    public CartDTO toDTO(CartEntity cartEntity){
//
//        return CartDTO.builder()
//                .cart_id(cartEntity.getCart_id())
//                .cartDetailItems(cartEntity.getCartDetailItems().stream().map(cartDetailConverter::toDTO).collect(Collectors.toList()))
//                .build();
//    }

    public CartDTO toDTO(CartEntity cartEntity){

        Integer quantityProduct = cartEntity.getCartDetailItems().stream()
                .mapToInt(CartDetailEntity::getAmount) // Lấy `amount` của từng `CartDetailEntity`
                .sum();

        Double totalPrice = cartEntity.getCartDetailItems().stream()
                .mapToDouble(cartDetailItem -> cartDetailItem.getAmount() * cartDetailItem.getProduct().getPrice()) // Tính tổng giá
                .sum();

        return CartDTO.builder()
                .cart_id(cartEntity.getCart_id())
                .cartDetailItems(cartEntity.getCartDetailItems().stream().map(cartDetailConverter::toDTO).collect(Collectors.toList()))
                .quantityProduct(quantityProduct)
                .totalPrice(totalPrice)
                .isMerge(false)
                .build();
    }

}
