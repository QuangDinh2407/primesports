package com.sportshop.ModalDTO;
import com.sportshop.Entity.ShopVoucherEntity;
import lombok.AllArgsConstructor;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDetailDTO {
    private String userOrderDetail_id;
    private ProductDTO product;
    private int amount;
    private float price;
    private ShopVoucherDTO shopVoucher;
}
