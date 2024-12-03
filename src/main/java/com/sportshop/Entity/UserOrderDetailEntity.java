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
	private UserOrderEntity userOrder;
	
	@ManyToOne
	@JoinColumn(name="product_id", referencedColumnName = "product_id")
	private ProductEntity product;

	@ManyToOne
	@JoinColumn(name="shopVoucher_id", referencedColumnName = "shopVoucher_id")
	private ShopVoucherEntity shopVoucher;

}
