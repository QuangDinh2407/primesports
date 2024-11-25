package com.sportshop.ModalDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sportshop.Contants.FormatDate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    private String name;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private Date birth;

    private String email;

    private String address;

    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String imagePath;

    private String gender;

    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date created_at;

    private String status;

    private AccountDTO account;

    public void setPhone(String phone) {
        this.phone = phone != null && !phone.trim().isEmpty() ? phone : null;  // Chỉ set nếu có giá trị
    }
}
