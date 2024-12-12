package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sportshop.Contants.FormatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Product")
public class ProductEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String product_id;

	@Column(nullable = false, columnDefinition = "nvarchar(255)")
	private String name;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private float import_price;

	@Column(nullable = false)
	private float price;

	@Column(nullable = false)
	private float rating;

	@Column(columnDefinition = "nvarchar(MAX)")
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date updated_at;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date deleted_at;

	@Column(nullable = false, columnDefinition = "nvarchar(255)")
	private String status;
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)

	@ToString.Exclude
	private List <ProductTypeDetailEntity> productTypeDetailItems = new ArrayList<ProductTypeDetailEntity>();
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	@ToString.Exclude
	private List <ProductImageEntity> productImageItems = new ArrayList<ProductImageEntity>();

	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <ProductReviewEntity> productReviewItems = new ArrayList<ProductReviewEntity>();
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <ShopImportDetailEntity> shopImportItems = new ArrayList<ShopImportDetailEntity>();
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <ShopExportDetailEntity> shopExportItems = new ArrayList<ShopExportDetailEntity>();
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <ShopVoucherDetailEntity> shopVoucherDetailItems = new ArrayList<ShopVoucherDetailEntity>();
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <CartDetailEntity> cartDetailItems = new ArrayList<CartDetailEntity>();
	
	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <UserOrderDetailEntity> UserOrderDetailItems = new ArrayList<UserOrderDetailEntity>();

	@OneToMany (mappedBy = "product", fetch = FetchType.LAZY)
	private List <SizeDetailEntity> SizeDetailItems = new ArrayList<SizeDetailEntity>();

}
