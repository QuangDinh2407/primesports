package com.sportshop.Service.Iml;

import com.sportshop.ModalDTO.CartDTO;
import com.sportshop.ModalDTO.CartDetailDTO;

public class CartDetailServiceIml {
    public String saveOrUpdateCartDetail(CartDetailDTO cartDetailDTO){
        CartDetailDTO cartDetail = new CartDetailDTO();
        cartDetail.setAmount(cartDetailDTO.getAmount());
        cartDetail.setProduct(cartDetailDTO.getProduct());
        cartDetail.setCart(cartDetailDTO.getCart());
        if(cartDetailDTO.getCartdetail_id()==null){
            saveOrUpdateCartDetail(cartDetail);
            return "Tạo mới thành công";
        }else{
            cartDetail.setCartdetail_id(cartDetailDTO.getCartdetail_id());
            saveOrUpdateCartDetail(cartDetail);
            return "Cập nhật thành công";
        }
    }
}

