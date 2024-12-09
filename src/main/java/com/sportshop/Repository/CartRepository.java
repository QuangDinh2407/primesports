package com.sportshop.Repository;

import com.sportshop.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    @Query("SELECT cart FROM CartEntity cart WHERE cart.cart_id = :cart_id")
    CartEntity findById(@Param("cart_id") String cart_id);

}
