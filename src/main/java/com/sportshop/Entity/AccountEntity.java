package com.sportshop.Entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="Account")
public class AccountEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String account_id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name="role_id", referencedColumnName = "role_id")
    private RoleEntity role;

    @OneToOne(mappedBy = "account")
    private UserInfoEntity user;

}
