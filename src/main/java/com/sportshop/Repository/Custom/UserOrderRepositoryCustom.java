package com.sportshop.Repository.Custom;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserOrderRepositoryCustom {

    @Query("""
        SELECT YEAR(uo.created_at) AS year, MONTH(uo.created_at) AS month, SUM(uo.totalPrice) AS totalRevenue
        FROM UserOrderEntity uo
        GROUP BY YEAR(uo.created_at), MONTH(uo.created_at)
        ORDER BY YEAR(uo.created_at) DESC, MONTH(uo.created_at) ASC
    """)
    List<Object[]> getTotalRevenueByMonth();

}