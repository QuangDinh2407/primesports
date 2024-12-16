package com.sportshop.Repository.Custom;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserOrderRepositoryCustom {

    @Query("""
    SELECT FUNCTION('YEAR', uo.created_at) AS year, 
           FUNCTION('MONTH', uo.created_at) AS month, 
           SUM(uo.totalPrice) 
    FROM UserOrderEntity uo
    WHERE FUNCTION('YEAR', uo.created_at) = (
        SELECT MAX(FUNCTION('YEAR', sub_uo.created_at)) 
        FROM UserOrderEntity sub_uo
    )
    GROUP BY FUNCTION('YEAR', uo.created_at), FUNCTION('MONTH', uo.created_at)
    ORDER BY FUNCTION('YEAR', uo.created_at) DESC, FUNCTION('MONTH', uo.created_at) ASC
""")
    List<Object[]> getTotalRevenueByMonth();

    @Query("""
    SELECT 
        FUNCTION('DAY', uo.created_at) AS day, 
        FUNCTION('MONTH', uo.created_at) AS month, 
        FUNCTION('YEAR', uo.created_at) AS year, 
        SUM(uo.totalPrice) AS totalRevenue 
    FROM 
        UserOrderEntity uo
    WHERE 
        FUNCTION('DAY', uo.created_at) = :day 
        AND FUNCTION('MONTH', uo.created_at) = :month 
        AND FUNCTION('YEAR', uo.created_at) = :year
    GROUP BY FUNCTION('DAY', uo.created_at), FUNCTION('MONTH', uo.created_at), FUNCTION('YEAR', uo.created_at)
""")
    List<Object[]> getTotalRevenueByDay(@Param("day") int day, @Param("month") int month, @Param("year") int year);

    @Query("""
    SELECT 
        FUNCTION('MONTH', uo.created_at) AS month, 
        FUNCTION('YEAR', uo.created_at) AS year, 
        SUM(uo.totalPrice) AS totalRevenue 
    FROM 
        UserOrderEntity uo
    WHERE 
        FUNCTION('MONTH', uo.created_at) = :month 
        AND FUNCTION('YEAR', uo.created_at) = :year
    GROUP BY FUNCTION('MONTH', uo.created_at), FUNCTION('YEAR', uo.created_at)
""")
    List<Object[]> getTotalRevenueByMonth(@Param("month") int month, @Param("year") int year);

    @Query("""
    
    SELECT 
    FUNCTION('YEAR', uo.created_at) AS year, 
    FUNCTION('MONTH', uo.created_at) AS month, 
    SUM(uo.totalPrice) 
        FROM UserOrderEntity uo
    WHERE 
        FUNCTION('YEAR', uo.created_at) = :year
    GROUP BY FUNCTION('YEAR', uo.created_at), FUNCTION('MONTH', uo.created_at)
    ORDER BY FUNCTION('YEAR', uo.created_at) DESC, FUNCTION('MONTH', uo.created_at) ASC
    
""")
    List<Object[]> getTotalRevenueByYear(@Param("year") int year);

    @Query("""
        SELECT COALESCE(SUM(uo.totalPrice), 0.0) 
        FROM UserOrderEntity uo 
        WHERE CAST(uo.created_at AS date) = CURRENT_DATE        
    """)
    Double getTotalRevenueToday();

    @Query("""
        SELECT COALESCE(SUM(uo.totalPrice), 0.0) 
        FROM UserOrderEntity uo
    """)
    Double getTotalRevenueOfShop();

    @Query("""
        SELECT p.product_id AS productId, p.name AS productName, SUM(uod.amount) AS totalAmount
        FROM UserOrderDetailEntity uod
        JOIN uod.product p
        GROUP BY p.product_id, p.name
        ORDER BY SUM(uod.amount) DESC
        FETCH FIRST 5 ROWS ONLY
    """)
    List<Object[]> findTopSellingProducts();


    @Query("""
        SELECT uo.status AS status, COUNT(uo.userOrder_id) AS orderCount
        FROM UserOrderEntity uo
        GROUP BY uo.status
    """)
    List<Object[]> getOrderCountByStatus();

    @Query("""
        SELECT uo.status AS status, COUNT(uo.userOrder_id) AS orderCount
        FROM UserOrderEntity uo
        WHERE 
            (:day IS NULL OR FUNCTION('DAY', uo.created_at) = :day) AND 
            (:month IS NULL OR FUNCTION('MONTH', uo.created_at) = :month) AND 
            (:year IS NULL OR FUNCTION('YEAR', uo.created_at) = :year)
        GROUP BY uo.status
    """)
    List<Object[]> getOrderCountByDay(
            @Param("day") Integer day,
            @Param("month") Integer month,
            @Param("year") Integer year
    );




}
