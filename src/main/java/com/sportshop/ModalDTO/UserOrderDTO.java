package com.sportshop.ModalDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDTO {
    private String userOrder_id;
    private String userEmail;
    private Date created_at;
    private String status;
    private Date updated_at;
    private PaymentTypeDTO paymentType;
    private String shipping_address;
    private String shipping_name;
    private String shipping_phone;
    private List<UserOrderDetailDTO> userOrderDetails;
    private Float total_price;
}
