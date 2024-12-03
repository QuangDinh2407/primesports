package com.sportshop.Converter;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Entity.ProductReviewEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductReviewConverter {

    public ProductReviewEntity toEntity(String comment, Float rating, String productId, String userId) {
        ProductReviewEntity review = new ProductReviewEntity();

        review.setComment(comment);
        review.setRating(rating);
        review.setCreated_at(new Date());
        review.setUpdated_at(new Date());

        // Thiết lập quan hệ với ProductEntity và UserInfoEntity
        ProductEntity product = new ProductEntity();
        product.setProduct_id(productId);
        review.setProduct(product);

        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUserInfo_id(userId);
        review.setUserInfo(userInfo);

        return review;
    }
}
