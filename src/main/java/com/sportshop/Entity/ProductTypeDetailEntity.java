package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "ProductTypeDetail")
public class ProductTypeDetailEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String productTypeDetail_id;
	
	@ManyToOne
	@JoinColumn(name="product_id", referencedColumnName = "product_id")
	private ProductEntity product;
	
	@ManyToOne
	@JoinColumn(name="productType_id", referencedColumnName = "productType_id")
	@ToString.Exclude
	private ProductTypeEntity productType;

}
