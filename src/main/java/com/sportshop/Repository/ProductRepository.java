package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query("SELECT DISTINCT p FROM ProductEntity p " +
            "LEFT JOIN p.productTypeDetailItems ptd " +
            "LEFT JOIN ptd.productType pt " +
            "WHERE (:productTypes IS NULL OR pt.productType_id IN :productTypes) " +
            "AND (:beginPrice IS NULL OR :endPrice IS NULL OR p.price BETWEEN :beginPrice AND :endPrice) " +
            "AND (:status IS NULL OR " +
            "(:status = 'IN_STOCK' AND p.quantity > 50) OR " +
            "(:status = 'LOW_STOCK' AND p.quantity BETWEEN 1 AND 50) OR " +
            "(:status = 'OUT_OF_STOCK' AND p.quantity = 0))")
    List<ProductEntity> filterProducts(
            @Param("productTypes") List<String> productTypes,
            @Param("beginPrice") Double beginPrice,
            @Param("endPrice") Double endPrice,
            @Param("status") String status
    );
}
