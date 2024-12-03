package com.sportshop.ModalDTO;

import com.sportshop.Contants.FormatDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class AccountDTO {

	@NotEmpty(message = "Email không được để trống")
	private String email;

	private String password;

	private RoleDTO role;

	private String is_disable;

	private String otp_code;

	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date expiry_date;

	@Valid
	private UserDTO userInfo;

}
