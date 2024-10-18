package com.sportshop.Entity;

import java.util.Date;

import com.sportshop.Contants.FomatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name ="ShopVoucherDetail")
public class ShopVoucherDetailEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopVoucherDetail_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date updated_at;
	
	@ManyToOne
	@JoinColumn(name="product_id", referencedColumnName = "product_id")
	private ProductEntity product;
	
	@ManyToOne
	@JoinColumn(name="shopVoucher_id", referencedColumnName = "shopVoucher_id")
	private ShopVoucherEntity shopVoucher;

}
