package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.SizeDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SizeDetailRepository extends JpaRepository<SizeDetailEntity, String> {
    @Transactional
    @Modifying
    @Query("DELETE FROM SizeDetailEntity s WHERE s.product = :product")
    void deleteByProduct(@Param("product") ProductEntity product);

    @Query("SELECT sd FROM SizeDetailEntity sd WHERE sd.product.product_id = :productId AND sd.size.size_id = :sizeId")
    SizeDetailEntity findByProductIdAndSizeId(@Param("productId") String productId, @Param("sizeId") String sizeId);
}
