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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<SizeDetailEntity> SizeDetailItems = new ArrayList<SizeDetailEntity>();

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private  List<UserOrderDetailEntity> listUserOrderDetailEntity;

}
