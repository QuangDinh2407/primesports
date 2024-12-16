package com.sportshop.Repository;

import com.sportshop.Entity.UserOrderEntity;
import com.sportshop.Repository.Custom.UserOrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrderEntity, String>, UserOrderRepositoryCustom {

    @Query("SELECT uo FROM UserOrderEntity uo WHERE uo.userInfo.userInfo_id = :userInfoId")
    List<UserOrderEntity> findAllOrdersByUser(@Param("userInfoId") String userInfoId);

//    @Query("SELECT uo FROM UserOrderEntity uo WHERE uo.userOrder_id = :orderId")
//    UserOrderEntity findById (@Param("orderId") String orderId));
}
