package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;

import com.sportshop.Entity.ShopVoucherEntity;
import com.sportshop.ModalDTO.ShopVoucherDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShopVoucherDetailRepository extends JpaRepository<ShopVoucherDetailEntity, String> {
    List<ShopVoucherDetailEntity> findAll();

    @Query("SELECT svd " +
            "FROM ShopVoucherDetailEntity svd " +
            "WHERE svd.product.product_id = :productId " +
            "AND svd.shopVoucher.shopVoucher_id = :voucherId")
    ShopVoucherDetailEntity findShopVoucherDetailByVoucherAndProduct(@Param("voucherId") String voucherId,
                                                                     @Param("productId") String productId);

    @Query("SELECT svd.product " +
            "FROM ShopVoucherDetailEntity svd " +
            "WHERE svd.shopVoucher.shopVoucher_id = :voucherId" )
    List<ProductEntity> findProductByShopVoucherId(@Param("voucherId") String voucherId);

    @Modifying
    @Transactional
    @Query("delete from ShopVoucherDetailEntity svd where svd.shopVoucher.shopVoucher_id = :voucherId")
    void deleteProductInVoucher(@Param("voucherId") String voucherId);
}
