package com.sportshop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sportshop.Contants.FormatDate;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String is_disable;

    @ManyToOne
    @JoinColumn(name="role_id", referencedColumnName = "role_id")
    private RoleEntity role;

    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true, nullable = false, referencedColumnName = "account_id")
    @ToString.Exclude
    private UserInfoEntity user;

    private String otp_code;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = FormatDate.FM_DATE)
    private Date expiry_date;

    public void setUser(UserInfoEntity user) {
        this.user = user;
        if (user != null) {
            user.setAccount(this);
        }
    }

}
