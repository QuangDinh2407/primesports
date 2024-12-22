package com.sportshop.Repository;

import com.sportshop.Entity.CartDetailEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartDetailRepository extends JpaRepository<CartDetailEntity, String> {

    @Query("SELECT c FROM CartDetailEntity c  WHERE c.cartdetail_id = :cartDetailId")
    CartDetailEntity findByCartDetailId(@Param("cartDetailId") String cartDetailId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetailEntity c WHERE c.cartdetail_id = :id")
    void deleteByCartDetailId(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetailEntity c WHERE c.cart.cart_id = :cartId")
    void deleteByCartId(@Param("cartId") String cartId);

}
