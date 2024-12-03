package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sportshop.Entity.ProductImageEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, String> {
    List<ProductImageEntity> findAll();
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImageEntity p WHERE p.product = :product")
    void deleteByProduct(@Param("product") ProductEntity product);

}
