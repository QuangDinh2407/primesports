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
@Table (name ="ShopCustomerVoucher")
public class ShopCustomerVoucherEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopCustomerVoucher_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	@Column(nullable = false)
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date updated_at;
	
	@ManyToOne
	@JoinColumn(name="userInfo_id", referencedColumnName = "userInfo_id")
	private UserInfoEntity userInfo;
	
	@ManyToOne
	@JoinColumn(name="shopVoucher_id", referencedColumnName = "shopVoucher_id")
	private ShopVoucherEntity shopVoucher;

	
}
