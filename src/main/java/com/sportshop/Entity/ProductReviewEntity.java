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
@Table(name = "ProductReview")
public class ProductReviewEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String productReview_id;

	@Column(nullable = false)
	private Float rating;
	
	private String comment;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date updated_at;
	
	@ManyToOne
	@JoinColumn(name="product_id",referencedColumnName = "product_id")
	private ProductEntity product;
	
	@ManyToOne
	@JoinColumn(name="userInfo_id", referencedColumnName = "userInfo_id")
	private UserInfoEntity userInfo;


}
