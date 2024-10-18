package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "ShopExportDetail")
public class ShopExportDetailEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopExportDetail_id;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Float unit_price;
	
	@ManyToOne
	@JoinColumn(name="shopExport_id", referencedColumnName = "shopExport_id")
	private ShopExportEntity shopExport;
	
	@ManyToOne
	@JoinColumn(name="product_id", referencedColumnName = "product_id")
	private ProductEntity product;


}
