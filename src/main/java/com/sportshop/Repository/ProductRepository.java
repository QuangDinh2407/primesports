package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,String> {

        @Query("SELECT pe " +
             "FROM ProductEntity pe " +
            "WHERE pe.product_id = :product_id" )
        ProductEntity findProductByID(@Param("product_id") String product_id);

}
