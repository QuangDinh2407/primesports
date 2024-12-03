package com.sportshop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private float price;

    @ManyToOne
    @JoinColumn(name="size_id", referencedColumnName = "size_id")
    private SizeEntity size;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private ProductEntity product;
}
