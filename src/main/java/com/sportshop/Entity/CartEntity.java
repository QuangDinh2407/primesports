package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Cart")
public class CartEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String cart_id;
		
	@OneToMany (mappedBy = "cart", fetch = FetchType.LAZY)
	private List <CartDetailEntity> cartDetailItems = new ArrayList<CartDetailEntity>();
	
	@OneToOne(mappedBy = "cart",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", unique = true, nullable = false, referencedColumnName = "user_id")
	private UserInfoEntity user;

}
