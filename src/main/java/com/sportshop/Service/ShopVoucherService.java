package com.sportshop.Service;

import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.ShopVoucherDTO;
import com.sportshop.ModalDTO.UserOrderDTO;

import java.util.List;

public interface ShopVoucherService {
    List<ShopVoucherDTO> findAll ();

    public String saveOrUpdateVoucher(ShopVoucherDTO svDTO);

    String deleteInfoVoucher(ShopVoucherDTO svDTO);

    Result findByCodeAndProductId(String code, UserOrderDTO userOrderDTO);
    
    public ShopVoucherDTO findByIdVoucher(String idVoucher);

}