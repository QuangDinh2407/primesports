package com.sportshop.ModalDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank( message = "Địa chỉ không được trống!")
    private String shipping_address;

    @NotBlank(message = "Tên không được trống!")
    private String shipping_name;

    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    private String shipping_phone;
 
    private String email;
    private List<UserOrderDetailDTO> userOrderDetails; // Danh sách chi tiết đơn hàng
    private Float total_price;
}
