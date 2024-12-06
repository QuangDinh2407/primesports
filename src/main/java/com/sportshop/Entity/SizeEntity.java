package com.sportshop.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Size")
public class SizeEntity {
    @Id
    private String size_id;

    @Column(nullable = false)
    private String name_size;

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    private List<SizeDetailEntity> SizeDetailItems = new ArrayList<SizeDetailEntity>();

    @OneToOne(mappedBy = "size", fetch = FetchType.LAZY)
    @JoinColumn(name = "userOrderDetail_id", unique = true, nullable = false, referencedColumnName = "userOrderDetail_id")
    @ToString.Exclude
    private  UserOrderDetailEntity userOrderDetailEntity;

}
