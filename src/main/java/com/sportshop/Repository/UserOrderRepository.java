package com.sportshop.Repository;

import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.Repository.Custom.UserOrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrderEntity, String>, UserOrderRepositoryCustom {

    @Query("SELECT uo FROM UserOrderEntity uo WHERE uo.userInfo.userInfo_id = :userInfoId")
    List<UserOrderEntity> findAllOrdersByUser(@Param("userInfoId") String userInfoId);

//    @Query("SELECT uo FROM UserOrderEntity uo WHERE uo.userOrder_id = :orderId")
//    UserOrderEntity findById (@Param("orderId") String orderId));
    @Query("SELECT uo FROM UserOrderEntity uo " +
            "WHERE (:status IS NULL OR uo.status = :status) "
            + "AND (:beginPrice IS NULL OR uo.totalPrice >= :beginPrice) " +
            "AND (:endPrice IS NULL OR uo.totalPrice <= :endPrice) " +
            "AND (:beginDate IS NULL OR uo.created_at >= :beginDate) " +
            "AND (:endDate IS NULL OR uo.created_at <= :endDate)")
    List<UserOrderEntity> filterOrders(@Param("status") String status,
                                       @Param("beginPrice") Float beginPrice,
                                       @Param("endPrice") Float endPrice,
                                       @Param("beginDate") Date beginDate,
                                       @Param("endDate") Date endDate);
}
