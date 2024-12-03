package com.sportshop.Converter;

import com.sportshop.Entity.PaymentTypeEntity;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.ModalDTO.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserOrderConverter {
    // Convert UserOrderEntity to UserOrderDTO
    public UserOrderDTO toDTO(UserOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        // Convert nested objects (PaymentType, Product, UserOrderDetail) to their DTOs
        PaymentTypeDTO paymentTypeDTO = PaymentTypeDTO.builder()
                .name(entity.getPaymentType().getName())
                .build();

        return UserOrderDTO.builder()
                .userOrder_id(entity.getUserOrder_id())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .status(entity.getStatus())
                .paymentType(paymentTypeDTO)
                .shipping_address(entity.getShipping_address())
                .userOrderDetails(entity.getUserOrderDetailItems().stream()
                        .map(detail -> UserOrderDetailDTO.builder()
                                .amount(detail.getAmount())
                                .price(detail.getPrice())
                                .product(ProductDTO.builder()
                                        .product_id(detail.getProduct().getProduct_id())
                                        .rating(detail.getProduct().getRating())
                                        .name(detail.getProduct().getName())
                                        .build())
                                .shopVoucher(ShopVoucherDTO.builder()
                                        .shopVoucher_id(detail.getShopVoucher().getShopVoucher_id())
                                        .discount_amount(detail.getShopVoucher().getDiscountAmount())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .total_price(entity.getTotalPrice())
                .build();
    }

    // Convert UserOrderDTO to UserOrderEntity
    public UserOrderEntity toEntity(UserOrderDTO dto) {
        if (dto == null) {
            return null;
        }

        UserOrderEntity entity = new UserOrderEntity();
        entity.setUserOrder_id(dto.getUserOrder_id());
        entity.setCreated_at(dto.getCreated_at());
        entity.setUpdated_at(dto.getUpdated_at());
        entity.setStatus(dto.getStatus());

        // Setting PaymentTypeEntity manually if required
        if (dto.getPaymentType() != null) {
            entity.setPaymentType(new PaymentTypeEntity());
            entity.getPaymentType().setName(dto.getPaymentType().getName());
        }

        return entity;
    }
}
