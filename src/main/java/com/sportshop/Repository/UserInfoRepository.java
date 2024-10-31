package com.sportshop.Repository;

import java.util.List;
import java.util.Optional;

import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Repository.Custom.AccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String>, AccountRepositoryCustom {
	List<UserInfoEntity> findAll();
	Optional<UserInfoEntity> findById(String id);
	UserInfoEntity findByEmail (String email);
}
