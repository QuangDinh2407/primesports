package com.sportshop.Service;

import com.sportshop.ModalDTO.CartDetailDTO;

public interface CartDetailService {
    public String saveOrUpdateCartDetail(CartDetailDTO cartDetailDTO);

    String deleteInfoCartDetail(CartDetailDTO cartDetailDTO);

}
