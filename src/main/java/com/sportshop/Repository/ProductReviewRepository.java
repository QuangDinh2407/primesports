package com.sportshop.Repository;

import com.sportshop.Entity.ProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, String> {
    @Query("SELECT COUNT(pr) > 0 FROM ProductReviewEntity pr WHERE pr.product.product_id = :productId AND pr.userInfo.userInfo_id = :userId")
    boolean existsByProductIdAndUserId(@Param("productId") String productId, @Param("userId") String userId);
}