package com.sportshop.Repository;

import com.sportshop.Entity.AccountEntity;
import com.sportshop.Repository.Custom.AccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity,String>, AccountRepositoryCustom {
     AccountEntity findByemail(String email);

     boolean existsByemail(String email);
}
