package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Entity;

import com.sportshop.Contants.FormatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "UserOrder")
public class UserOrderEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String userOrder_id;

	@Column(nullable = false, columnDefinition = "nvarchar(MAX)")
	private String status; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	@Column(nullable = false)
	private Date created_at;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date updated_at;

	@Column(nullable = false)
	private  Float totalPrice;

	@Column(nullable = false, columnDefinition = "nvarchar(MAX)")
	private String shipping_address;

	@ManyToOne
	@JoinColumn(name="userInfo_id", referencedColumnName = "userInfo_id")
	private UserInfoEntity userInfo;
	
	@ManyToOne
	@JoinColumn(name="paymentType_id", referencedColumnName = "paymentType_id")
	private PaymentTypeEntity paymentType;
	
	@OneToMany(mappedBy = "userOrder", fetch = FetchType.LAZY)
	private List <UserOrderDetailEntity> userOrderDetailItems = new ArrayList<UserOrderDetailEntity>();
}
