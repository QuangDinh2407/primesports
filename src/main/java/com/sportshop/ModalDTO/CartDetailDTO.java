package com.sportshop.ModalDTO;

import com.sportshop.Entity.CartEntity;
import com.sportshop.Entity.ProductEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDTO {

    private String cartdetail_id;

    private Integer amount;

    @ToString.Exclude
    private CartDTO cart;

    private ProductDTO product;
}
