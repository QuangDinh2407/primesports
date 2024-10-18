package com.sportshop.ModalDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;

    private Date birth;

    private String email;

    private String phone;

    private Date created_at;

    private String status;

    private AccountDTO account;
}
