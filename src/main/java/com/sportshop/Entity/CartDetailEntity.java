package com.sportshop.Entity;



import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="CartDetail")
public class CartDetailEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String cartdetail_id;

	@Column(nullable = false)
	private Integer amount;
	
	@ManyToOne
	@JoinColumn(name="cart_id", referencedColumnName = "cart_id")
	private CartEntity cart;
	
	@ManyToOne
	@JoinColumn(name="product_id",referencedColumnName = "product_id")
	private ProductEntity product;

	
}
