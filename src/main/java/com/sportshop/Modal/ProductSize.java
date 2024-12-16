package com.sportshop.Modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize {
    String productId;
    String sizeId;
    Integer amount;
}
