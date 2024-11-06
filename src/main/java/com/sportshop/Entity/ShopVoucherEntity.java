package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sportshop.Contants.FormatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name ="ShopVoucher")
public class ShopVoucherEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopVoucher_id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;
	
	private String description;

	@Column(nullable = false)
	private float discountAmount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	@Column(nullable = false)
	private Date started_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	@Column(nullable = false)
	private Date ended_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	@Column(nullable = false)
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date updated_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date deleted_at;

	@Column(nullable = false)
	private String status;
	
	@OneToMany (mappedBy = "shopVoucher", fetch = FetchType.LAZY)
	private List <ShopVoucherDetailEntity> shopVoucherDetailItems = new ArrayList<ShopVoucherDetailEntity>();
	
	@OneToMany (mappedBy = "shopVoucher", fetch = FetchType.LAZY)
	private List <ShopCustomerVoucherEntity> shopCustomerVoucherItems = new ArrayList<ShopCustomerVoucherEntity>();


	
}
