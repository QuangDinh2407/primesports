package com.sportshop.Repository;

import com.sportshop.Entity.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductTypeEntity, String> {
    ProductTypeEntity findByName(String name);

    List<ProductTypeEntity> findAll();

//    List<ProductTypeEntity> findByNameIn(List<String> names);

    Page<ProductTypeEntity> findByNameIn(List<String> names, Pageable pageable);
}
