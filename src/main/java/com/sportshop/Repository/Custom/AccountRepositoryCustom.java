package com.sportshop.Repository.Custom;


import com.sportshop.Entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepositoryCustom {

    @Query("SELECT a FROM AccountEntity a " +
            "WHERE (:search = '' OR a.email LIKE %:search%) " +
            "AND (:status = 'all' OR :status = '' OR a.is_disable = :status) " +
            "AND a.role.name = 'CUSTOMER'")
    Page<AccountEntity> findBySearchAndStatus(@Param("search") String search, @Param("status") String status, Pageable pageable);



}
