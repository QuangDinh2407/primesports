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
@Table (name = "ShopExport")
public class ShopExportEntity {

	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private String shopExport_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date import_date;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	@Column(nullable = false)
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = FormatDate.FM_DATE)
	private Date updated_at;

	@Column(nullable = false, columnDefinition = "nvarchar(255)")
	private String status;
	
	@OneToMany (mappedBy = "shopExport", fetch = FetchType.LAZY)
	private List <ShopExportDetailEntity> shopExportItems = new ArrayList<ShopExportDetailEntity>();
	
	@ManyToOne
	@JoinColumn(name="userInfo_id", referencedColumnName = "userInfo_id")
	private UserInfoEntity userInfo;


}
