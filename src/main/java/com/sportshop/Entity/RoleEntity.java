package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "Role")
public class RoleEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String role_id;

	@Column(nullable = false, columnDefinition = "nvarchar(255)")
	private String name;
	
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private List <AccountEntity> accountItems = new ArrayList<AccountEntity>();
	
}
