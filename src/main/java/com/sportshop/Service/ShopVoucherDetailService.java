package com.sportshop.Service;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.ModalDTO.ShopVoucherDTO;
import com.sportshop.ModalDTO.ShopVoucherDetailDTO;

import java.util.List;

public interface ShopVoucherDetailService {
    List<ShopVoucherDetailDTO> findAll();

    public List<String> productToString(List<ProductEntity> existProduct);

    public String saveOrUpdateVoucherDetail(ShopVoucherDetailDTO svdDTO,List<String> productId, String voucherId);

    String deleteInfoVoucherDetail(ShopVoucherDTO svdDTO);
}