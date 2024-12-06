package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Repository.Custom.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<ProductEntity,String>, ProductRepositoryCustom {

//    List<ProductEntity> findTop5ByOrderByRatingDesc();

    List<ProductEntity> findAll();

    Page<ProductEntity> findAll(Pageable pageable);

    List<ProductEntity> findTop5ByQuantityGreaterThanOrderByRatingDesc(int quantity);

    @Query("SELECT p FROM ProductEntity p WHERE p.product_id = :productId")
    ProductEntity findByProductId(@Param("productId") String productId);


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

    @Query("SELECT p FROM ProductEntity p WHERE p.product_id IN :productIds")
    List<ProductEntity> findByProductIds(@Param("productIds") List<String> productIds);


}
