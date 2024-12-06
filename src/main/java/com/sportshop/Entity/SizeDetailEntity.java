package com.sportshop.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SizeDetail")
public class SizeDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sizeDetail_id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name="size_id", referencedColumnName = "size_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SizeEntity size;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductEntity product;

}
