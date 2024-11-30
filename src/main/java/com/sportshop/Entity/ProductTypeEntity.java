package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "ProductType")
public class ProductTypeEntity {
	@Id
	private String productType_id;

	@Column(nullable = false, columnDefinition = "nvarchar(255)")
	private String name;

	private String parent_id;
	
	@OneToMany (mappedBy = "productType", fetch = FetchType.LAZY)
	@ToString.Exclude
	private List <ProductTypeDetailEntity> productTypeDetailItems = new ArrayList<ProductTypeDetailEntity>();


	
}
