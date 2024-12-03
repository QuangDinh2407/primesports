package com.sportshop.ModalDTO;
import lombok.AllArgsConstructor;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTypeDTO {
    private String paymentType_id;
    private String name;
}
