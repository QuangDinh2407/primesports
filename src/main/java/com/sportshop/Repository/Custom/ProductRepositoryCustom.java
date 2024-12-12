package com.sportshop.Repository.Custom;

import com.sportshop.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepositoryCustom {

    @Query("SELECT p FROM ProductEntity p " +
            "JOIN p.productTypeDetailItems ptDetail " +
            "JOIN ptDetail.productType pt " +
            "WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:rating IS NULL OR p.rating >= :rating) " +
            "AND p.quantity > 0" +
            "AND (:types IS NULL OR pt.name IN :types) " +
            "ORDER BY p.price DESC")
    Page<ProductEntity> searchProducts(
            @Param("name") String name,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice,
            @Param("rating") Float rating,
            @Param("types") List<String> types,
            Pageable pageable);

    @Query("""
        SELECT SUM(p.quantity * p.import_price) 
        FROM ProductEntity p
    """)
    Double getTotalImportPrice();

}
