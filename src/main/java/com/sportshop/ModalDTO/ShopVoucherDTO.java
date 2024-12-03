package com.sportshop.ModalDTO;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopVoucherDTO {
    private String shopVoucher_id;

    private float discount_amount;

    private String status = "active";

}
