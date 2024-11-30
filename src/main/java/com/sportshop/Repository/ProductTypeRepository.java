package com.sportshop.Repository;

import com.sportshop.Entity.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductTypeEntity, String> {
    ProductTypeEntity findByName(String name);

    List<ProductTypeEntity> findByNameIn(List<String> names);
}
