package com.sportshop.ModalDTO;

import com.sportshop.Contants.FormatDate;
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
    private Date birth;

    private String email;

    private String address;

    private String phone;

    private String imagePath;

    private Date created_at;

    private String status;

    private AccountDTO account;

    private String gender;

    private CartDTO cart;
}
