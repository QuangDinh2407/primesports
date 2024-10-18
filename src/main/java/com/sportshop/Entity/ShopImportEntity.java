package com.sportshop.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sportshop.Contants.FomatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "ShopImport")
public class ShopImportEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopImport_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date import_date;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	@Column(nullable = false)
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FomatDate.FM_DATE)
	private Date updated_at;

	@Column(nullable = false)
	private String status;
	
	@OneToMany (mappedBy = "shopImport", fetch = FetchType.LAZY)
	private List <ShopImportDetailEntity> shopImportDetailItems = new ArrayList<ShopImportDetailEntity>();

	@ManyToOne
	@JoinColumn(name="userInfo_id", referencedColumnName = "userInfo_id")
	private UserInfoEntity userInfo;
	
}
