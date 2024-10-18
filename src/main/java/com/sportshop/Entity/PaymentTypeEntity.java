package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "PaymentType")
public class PaymentTypeEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String paymentType_id;

	@Column(nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "paymentType")
	private List <UserOrderEntity> userOrderItems = new ArrayList<UserOrderEntity>();
	
	
}
