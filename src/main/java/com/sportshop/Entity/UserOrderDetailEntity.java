package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "UserOrderDetail")
public class UserOrderDetailEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String userOrderDetail_id;

	@Column(nullable = false)
	private Integer amount;

	@Column(nullable = false)
	private float price;
	
	@ManyToOne
	@JoinColumn(name="userOrder_id", referencedColumnName = "userOrder_id")
	@ToString.Exclude
	private UserOrderEntity userOrder;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="product_id", referencedColumnName = "product_id")
	@ToString.Exclude
	private ProductEntity product;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="shopVoucher_id", referencedColumnName = "shopVoucher_id")
	@ToString.Exclude
	private ShopVoucherEntity shopVoucher;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "size_id", referencedColumnName = "size_id")
	@ToString.Exclude
	private SizeEntity size;

}
