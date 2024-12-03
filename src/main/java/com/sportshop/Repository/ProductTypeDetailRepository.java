package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sportshop.Entity.ProductTypeDetailEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductTypeDetailRepository extends JpaRepository<ProductTypeDetailEntity, String> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ProductTypeDetailEntity p WHERE p.product = :product")
    void deleteByProduct(@Param("product") ProductEntity product);
}
