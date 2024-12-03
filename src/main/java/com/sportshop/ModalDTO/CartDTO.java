package com.sportshop.ModalDTO;

import com.sportshop.Entity.CartDetailEntity;
import com.sportshop.Entity.UserInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private String cart_id;

    private List<CartDetailDTO> cartDetailItems=new ArrayList<>();

    private UserDTO user;


}
