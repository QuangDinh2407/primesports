package com.sportshop.Service;

import com.sportshop.Entity.ProductReviewEntity;
import com.sportshop.Repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface ProductReviewService {

    void save(ProductReviewEntity review);

    boolean hasReviewed(String productId, String userId);
}