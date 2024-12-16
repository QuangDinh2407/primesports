package com.sportshop.Entity;



import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name ="CartDetail")
public class CartDetailEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String cartdetail_id;

	@Column(nullable = false)
	private Integer amount;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "size_id", referencedColumnName = "size_id")
	@ToString.Exclude
	private SizeEntity size;
	
	@ManyToOne
	@JoinColumn(name="cart_id", nullable = false, referencedColumnName = "cart_id")
	@ToString.Exclude
	private CartEntity cart;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable = false, referencedColumnName = "product_id")
	@ToString.Exclude
	private ProductEntity product;

	
}
