package com.sportshop.Repository;

import com.sportshop.Entity.UserOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrderEntity, String> {

    @Query("SELECT uo FROM UserOrderEntity uo WHERE uo.userInfo.userInfo_id = :userInfoId")
    List<UserOrderEntity> findAllOrdersByUser(@Param("userInfoId") String userInfoId);
}
