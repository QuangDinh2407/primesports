package com.sportshop.Entity;

import com.sportshop.Contants.FomatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "UserInfo")
public class UserInfoEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String userInfo_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    private Date birth;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = FomatDate.FM_DATE)
    @Column(nullable = false)
    private Date created_at;

    @Column(nullable = false)
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", unique = true, referencedColumnName = "account_id")
    private AccountEntity account;

    // USER - EMPLOYEE
    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<ShopImportEntity> shopImportItems = new ArrayList<ShopImportEntity>();

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List <ShopExportEntity> shopExportItems = new ArrayList<ShopExportEntity>();

    // USER - CUSTOMER
    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List <ProductReviewEntity> productReviewItem;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List <ShopCustomerVoucherEntity> shopCustomerVoucherItems;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List <UserOrderEntity> customerOrderItems = new ArrayList<UserOrderEntity>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", unique = true, referencedColumnName = "cart_id")
    private CartEntity cart;

}
