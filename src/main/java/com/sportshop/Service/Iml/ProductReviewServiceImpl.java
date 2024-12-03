package com.sportshop.Service.Iml;

import com.sportshop.Converter.ProductReviewConverter;
import com.sportshop.Entity.ProductReviewEntity;
import com.sportshop.ModalDTO.ProductReviewDTO;
import com.sportshop.Repository.ProductReviewRepository;
import com.sportshop.Service.ProductReviewService;
import com.sportshop.Repository.ProductRepository;
import com.sportshop.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Override
    public void save(ProductReviewEntity review) {
        productReviewRepository.save(review);
    }

    @Override
    public boolean hasReviewed(String productId, String userId) {
        return productReviewRepository.existsByProductIdAndUserId(productId, userId);
    }

}
