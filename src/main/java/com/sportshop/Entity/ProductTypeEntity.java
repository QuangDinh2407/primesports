package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "ProductType")
public class ProductTypeEntity {
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String productType_id;

	@Column(nullable = false, columnDefinition = "nvarchar(255)")
	private String name;

	private String parent_id;
	
	@OneToMany (mappedBy = "productType", fetch = FetchType.LAZY)
	private List <ProductTypeDetailEntity> productTypeDetailItems = new ArrayList<ProductTypeDetailEntity>();


	
}
