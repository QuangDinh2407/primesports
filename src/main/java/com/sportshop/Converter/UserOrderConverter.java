package com.sportshop.Converter;

import com.sportshop.Entity.PaymentTypeEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Entity.ProductImageEntity;
import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.ModalDTO.*;
import com.sportshop.Repository.PaymentTypeRepository;
import com.sportshop.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserOrderConverter {

    @Autowired
    UserOrderDetailConverter userOrderDetailConverter;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

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
                                .size(detail.getSize().getName_size())
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
                                        .discountAmount(detail.getShopVoucher().getDiscountAmount())
                                        .build()
                                        : ShopVoucherDTO.builder()
                                        .discountAmount(0)
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
        
        entity.setTotalPrice(dto.getTotal_price()+30000);
        entity.setShipping_name(dto.getShipping_name());
        entity.setShipping_address(dto.getShipping_address());
        entity.setShipping_phone(dto.getShipping_phone());
        entity.setStatus("Chờ xác nhận");
        entity.setStatus(dto.getStatus());

        UserInfoEntity userInfo = userInfoRepository.findByEmail(dto.getUserEmail());
        entity.setUserInfo(userInfo);

        PaymentTypeEntity paymentTypeEntity = paymentTypeRepository.findByName(dto.getPaymentType().getName());
        entity.setPaymentType(paymentTypeEntity);

        entity.setUserOrderDetailItems(
                dto.getUserOrderDetails().stream()
                        .map(userOrderDetailDTO -> userOrderDetailConverter.toEntity(userOrderDetailDTO, entity))
                        .collect(Collectors.toList())
        );

        return entity;
    }
}
