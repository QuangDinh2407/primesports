package com.sportshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sportshop.Entity.ProductTypeDetailEntity;

public interface ProductTypeDetailRepository extends JpaRepository<ProductTypeDetailEntity, String> {
}
