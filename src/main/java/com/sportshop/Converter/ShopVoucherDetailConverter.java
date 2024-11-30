package com.sportshop.Converter;

import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.ModalDTO.ShopVoucherDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class ShopVoucherDetailConverter {

    public ShopVoucherDetailDTO toDTO(ShopVoucherDetailEntity entity) {
        if (entity == null) {
            return null;
        }
        return ShopVoucherDetailDTO.builder()
                .shopVoucherDetail_id(entity.getShopVoucherDetail_id())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .productName(entity.getProduct() != null ? entity.getProduct().getName() : null)
                .shopVoucherName(entity.getShopVoucher() != null ? entity.getShopVoucher().getName() : null)
                .build();
    }

    public ShopVoucherDetailEntity toEntity(ShopVoucherDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        ShopVoucherDetailEntity entity = new ShopVoucherDetailEntity();
        entity.setShopVoucherDetail_id(dto.getShopVoucherDetail_id());
        entity.setCreated_at(dto.getCreated_at());
        entity.setUpdated_at(dto.getUpdated_at());
        // Lưu ý: Không đặt trực tiếp `product` hoặc `shopVoucher` vì cần load từ DB
        return entity;
    }
}
