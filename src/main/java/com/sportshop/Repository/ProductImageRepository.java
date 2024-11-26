package com.sportshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sportshop.Entity.ProductImageEntity;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, String> {
}
