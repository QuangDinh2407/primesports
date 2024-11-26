package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name ="ProductImage")
public class ProductImageEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String productImage_id;

	@Column(nullable = false)
	private String image_path;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="product_id",nullable = false, referencedColumnName = "product_id")
	private ProductEntity product;


}
