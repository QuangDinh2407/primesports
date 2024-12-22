package com.sportshop.ModalDTO;


import com.sportshop.Contants.FormatDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String user_id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private Date birth;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String imagePath;

    @Pattern(regexp = "^(Nam|Nữ)$", message = "Giới tính không hợp lệ")
    private String gender;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date created_at;

    private String status;

    private AccountDTO account;

    private CartDTO cart;

    public void setPhone(String phone) {
        this.phone = phone != null && !phone.trim().isEmpty() ? phone : null;  // Chỉ set nếu có giá trị
    }
}
