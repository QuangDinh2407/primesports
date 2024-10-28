package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="Account")
public class AccountEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String account_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String isDisable;

    @ManyToOne
    @JoinColumn(name="role_id", referencedColumnName = "role_id")
    private RoleEntity role;

    @OneToOne(mappedBy = "account")
    @JoinColumn(name = "account_id", unique = true, nullable = false, referencedColumnName = "account_id")
    private UserInfoEntity user;

    @OneToMany (mappedBy = "account", fetch = FetchType.LAZY)
    private List<OTPEntity> OTPItems = new ArrayList<OTPEntity>();

}
