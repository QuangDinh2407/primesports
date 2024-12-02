package com.sportshop.Repository;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Repository.Custom.AccountRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,String>, AccountRepositoryCustom {
     AccountEntity findByemail(String email);

     boolean existsByemail(String email);

     List<AccountEntity> findAll();

     Optional<AccountEntity> findByEmail(String email);
}
