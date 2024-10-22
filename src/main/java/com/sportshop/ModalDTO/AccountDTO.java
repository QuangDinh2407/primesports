package com.sportshop.ModalDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

	private String email;

	private String password;
	
	private RoleDTO role;

}
