package com.sportshop.Service;

import com.sportshop.ModalDTO.ShopVoucherDTO;

import java.util.List;

public interface ShopVoucherService {
    List<ShopVoucherDTO> findAll ();

    public String saveOrUpdateVoucher(ShopVoucherDTO svDTO);

    String deleteInfoVoucher(ShopVoucherDTO svDTO);

}
