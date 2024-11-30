package com.sportshop.Repository;

import com.sportshop.Entity.ProductEntity;
import com.sportshop.Entity.ProductTypeEntity;
import com.sportshop.Entity.ShopVoucherDetailEntity;
import com.sportshop.Entity.ShopVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopVoucherRepository extends JpaRepository<ShopVoucherEntity, String> {
    List<ShopVoucherEntity> findAll();

    @Query("SELECT DISTINCT pt.name " +
            "FROM ProductTypeEntity pt " +
            "JOIN ProductTypeDetailEntity ptd ON pt.productType_id = ptd.productType.productType_id " +
            "JOIN ProductEntity p ON ptd.product.product_id = p.product_id " +
            "JOIN ShopVoucherDetailEntity svd ON p.product_id = svd.product.product_id " +
            "JOIN ShopVoucherEntity sv ON svd.shopVoucher.shopVoucher_id = sv.shopVoucher_id " +
            "WHERE sv.shopVoucher_id = :voucherId")
    List<String> findProductTypeNamesByVoucherId(@Param("voucherId") String voucherId);

    @Query("SELECT sv FROM ShopVoucherEntity sv WHERE sv.code = :code")
    ShopVoucherEntity findByCode(@Param("code") String code);

    @Query("SELECT svc " +
            "FROM ShopVoucherEntity svc " +
            "WHERE svc.shopVoucher_id = :shopVoucher_id" )
    ShopVoucherEntity findShopVoucherByID(@Param("shopVoucher_id") String shopVoucher_id);


}
