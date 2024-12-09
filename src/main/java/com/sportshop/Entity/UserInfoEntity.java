package com.sportshop.Entity;

import com.sportshop.Contants.FormatDate;
import com.sportshop.ModalDTO.UserDTO;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "UserInfo")
public class UserInfoEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String userInfo_id;

    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    private Integer age;

    @Column(columnDefinition = "nvarchar(255)")
    private String gender;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(columnDefinition = "nvarchar(255)")
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date birth;

    private String image_path;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date created_at;

    @Column(columnDefinition = "nvarchar(255)")
    private String status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true, referencedColumnName = "account_id")
    @ToString.Exclude
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", unique = true, referencedColumnName = "cart_id")
    @ToString.Exclude
    private CartEntity cart;

    public void setCart(CartEntity cart) {
        this.cart = cart;
        if (cart != null) {
            cart.setUser(this);
        }
    }

}
