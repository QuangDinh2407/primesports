package com.sportshop.Service;

import com.sportshop.Entity.ProductReviewEntity;
import com.sportshop.Repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;

    public void save(ProductReviewEntity review) {
        productReviewRepository.save(review);
    }
    public boolean hasReviewed(String productId, String userId) {
        return productReviewRepository.existsByProductIdAndUserId(productId, userId);
    }
}