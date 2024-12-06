package com.sportshop.Converter;

import com.sportshop.Entity.PaymentTypeEntity;
import com.sportshop.Entity.ProductImageEntity;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.ModalDTO.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class    UserOrderConverter {
    // Convert UserOrderEntity to UserOrderDTO
    public UserOrderDTO toDTO(UserOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        // Convert PaymentType to DTO
        PaymentTypeDTO paymentTypeDTO = PaymentTypeDTO.builder()
                .name(entity.getPaymentType() != null ? entity.getPaymentType().getName() : null)
                .build();

        // Return UserOrderDTO
        return UserOrderDTO.builder()
                .userOrder_id(entity.getUserOrder_id())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .status(entity.getStatus())
                .paymentType(paymentTypeDTO)
                .shipping_address(entity.getShipping_address())
                .email(entity.getUserInfo().getEmail()) // Ánh xạ thông tin người dùng
                .userOrderDetails(entity.getUserOrderDetailItems().stream()
                        .map(detail -> UserOrderDetailDTO.builder()
                                .amount(detail.getAmount())
                                .price(detail.getPrice())
                                .product(ProductDTO.builder()
                                        .product_id(detail.getProduct() != null ? detail.getProduct().getProduct_id() : null)
                                        .imagePaths(detail.getProduct() != null
                                                ? detail.getProduct().getProductImageItems().stream()
                                                .map(ProductImageEntity::getImage_path)
                                                .collect(Collectors.toList())
                                                : null)
                                        .rating(detail.getProduct() != null ? detail.getProduct().getRating() : null)
                                        .name(detail.getProduct() != null ? detail.getProduct().getName() : null)
                                        .build())
                                .shopVoucher(detail.getShopVoucher() != null
                                        ? ShopVoucherDTO.builder()
                                        .shopVoucher_id(detail.getShopVoucher().getShopVoucher_id())
                                        .discount_amount(detail.getShopVoucher().getDiscountAmount())
                                        .build()
                                        : ShopVoucherDTO.builder()
                                        .discount_amount(0)
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
