package com.sportshop.Converter;

import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.Entity.ShopVoucherEntity;
import com.sportshop.ModalDTO.ShopVoucherDTO;
import com.sportshop.Repository.ShopVoucherDetailRepository;
import com.sportshop.Repository.ShopVoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShopVoucherConverter {
    @Autowired
    ShopVoucherRepository shopVoucherRepo;

    @Autowired
    ShopVoucherDetailRepository shopVoucherDetailRepository;
    @Autowired
    private ShopVoucherRepository shopVoucherRepository;

    public String findProductTypeNamesByVoucherId(String voucherId) {
        List<String> productTypeNames = shopVoucherRepo.findProductTypeNamesByVoucherId(voucherId);
        String result = productTypeNames.stream()
                .collect(Collectors.joining(", "));  // Nối các phần tử bằng dấu phẩy và khoảng trắng

        // Kiểm tra độ dài của chuỗi, nếu dài hơn 17 ký tự thì cắt và thêm "..."
        if (result.length() > 30) {
            return result.substring(0,30) + "...";
        }
        return result;
    }

    public ShopVoucherDTO toDTO(ShopVoucherEntity shopVoucherEntity){

        return ShopVoucherDTO.builder()
                .shopVoucher_id(shopVoucherEntity.getShopVoucher_id())
                .name(shopVoucherEntity.getName())
                .code(shopVoucherEntity.getCode())
                .description(shopVoucherEntity.getDescription())
                .discountAmount(shopVoucherEntity.getDiscountAmount())
                .started_at(shopVoucherEntity.getStarted_at())
                .ended_at(shopVoucherEntity.getEnded_at())
                .created_at(shopVoucherEntity.getCreated_at())
                .updated_at(shopVoucherEntity.getUpdated_at())
                .deleted_at(shopVoucherEntity.getDeleted_at())
                .status(shopVoucherEntity.getStatus())
                .shopVoucherDetailItems(shopVoucherEntity.getShopVoucherDetailItems()) // Nếu cần ánh xạ sâu hơn, thêm logic tại đây
                .shopCustomerVoucherItems(shopVoucherEntity.getShopCustomerVoucherItems())
                .productTypeName(findProductTypeNamesByVoucherId(shopVoucherEntity.getShopVoucher_id()))
//                .findSVDByShopVoucher_id(findDetailByshopVoucherName(shopVoucherEntity.getShopVoucher_id()))
                // Nếu cần ánh xạ sâu hơn, thêm logic tại đây
                .build();
    }


}