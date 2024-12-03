package com.sportshop.Repository.Custom;

import com.sportshop.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepositoryCustom extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {

}
