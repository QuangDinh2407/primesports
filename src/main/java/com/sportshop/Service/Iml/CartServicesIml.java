package com.sportshop.Service.Iml;

import com.sportshop.ModalDTO.CartDTO;

public class CartServicesIml {
    public String saveOrUpdateCart(CartDTO cartDTO){
        CartDTO cart = new CartDTO();
        if(cart.getCart_id()==null){
            saveOrUpdateCart(cart);
            return "Tạo mới thành công";
        }else{
            cart.setCart_id(cart.getCart_id());
            saveOrUpdateCart(cart);
            return "Cập nhật thành công";
        }
    }
}
