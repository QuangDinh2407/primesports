package com.sportshop.Repository;

import com.sportshop.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, String> {

    @Query("SELECT cart FROM CartEntity cart WHERE cart.cart_id = :cart_id")
    CartEntity findByCartId(@Param("cart_id") String cart_id);

    @Query("SELECT c.cart_id FROM CartEntity c")
    List<String> findAllCartIds();


}
