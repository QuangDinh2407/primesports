package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "ShopImportDetail")
public class ShopImportDetailEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopImportDetail_id;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Float unit_price;
	
	@ManyToOne
	@JoinColumn(name="shopImport_id", referencedColumnName = "shopImport_id")
	private ShopImportEntity shopImport;
	
	@ManyToOne
	@JoinColumn(name="product_id", referencedColumnName = "product_id")
	private ProductEntity product;

	
}
