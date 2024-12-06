package com.sportshop.Modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {
    private String productId;
    private String size;
    private Integer amount;
}
