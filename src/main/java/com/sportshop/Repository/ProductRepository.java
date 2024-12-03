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

    List<ProductEntity> findTop5ByStatusOrderByRatingDesc(String status);

    @Query("SELECT p FROM ProductEntity p WHERE p.product_id = :productId")
    ProductEntity findByProductId(@Param("productId") String productId);

}
