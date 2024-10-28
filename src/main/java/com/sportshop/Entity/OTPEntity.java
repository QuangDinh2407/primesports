package com.sportshop.Entity;

import com.sportshop.Contants.FomatDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OTP")
public class OTPEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String otp_id;

    @Column(nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name="account_id", nullable = false, referencedColumnName = "account_id")
    private AccountEntity account;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = FomatDate.FM_DATE)
    @Column(nullable = false)
    private Date expiry_date;

}
